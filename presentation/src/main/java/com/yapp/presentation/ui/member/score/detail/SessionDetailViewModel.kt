package com.yapp.presentation.ui.member.score.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetMemberAttendanceListUseCase
import com.yapp.presentation.model.Attendance.Companion.mapTo
import com.yapp.presentation.model.Session.Companion.mapTo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SessionDetailViewModel @Inject constructor(
    private val getMemberAttendanceListUseCase: GetMemberAttendanceListUseCase,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<SessionDetailContract.SessionDetailUiState, SessionDetailContract.SessionDetailUiSideEffect, SessionDetailContract.SessionDetailUiEvent>(
    SessionDetailContract.SessionDetailUiState()
) {

    init {
        setState { copy(loadState = SessionDetailContract.SessionDetailUiState.LoadState.Loading) }
        val sessionId = savedStateHandle.get<Int>("session")
        if (sessionId != null) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    getMemberAttendanceListUseCase()
                        .collectWithCallback(
                            onSuccess = { entities ->
                                val session = entities.first.map { entity -> entity.mapTo() }
                                val attendance = entities.second?.map { entity -> entity.mapTo() }
                                if (!attendance.isNullOrEmpty()) {
                                    val attendanceList = session zip attendance
                                    setState { copy(loadState = SessionDetailContract.SessionDetailUiState.LoadState.Idle, session = attendanceList[sessionId]) }
                                } else {
                                    setState { copy(loadState = SessionDetailContract.SessionDetailUiState.LoadState.Error) }
                                }
                            },
                            onFailed = {
                                setState { copy(loadState = SessionDetailContract.SessionDetailUiState.LoadState.Error) }
                            }
                        )
                }
            }
        } else {
            setState { copy(loadState = SessionDetailContract.SessionDetailUiState.LoadState.Error) }
        }
    }

    override suspend fun handleEvent(event: SessionDetailContract.SessionDetailUiEvent) {
        TODO("Not yet implemented")
    }

}