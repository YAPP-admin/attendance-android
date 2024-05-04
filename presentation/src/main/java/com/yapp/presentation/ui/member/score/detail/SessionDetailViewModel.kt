package com.yapp.presentation.ui.member.score.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetCurrentTimeUseCase
import com.yapp.domain.usecases.GetMemberAttendanceListUseCase
import com.yapp.presentation.common.AttendanceTypeMapper
import com.yapp.presentation.ui.member.score.detail.SessionDetailContract.SessionDetailUiEvent
import com.yapp.presentation.ui.member.score.detail.SessionDetailContract.SessionDetailUiSideEffect
import com.yapp.presentation.ui.member.score.detail.SessionDetailContract.SessionDetailUiState
import com.yapp.presentation.ui.member.score.detail.SessionDetailContract.SessionDetailUiState.SessionDetailScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SessionDetailViewModel @Inject constructor(
    private val getMemberAttendanceListUseCase: GetMemberAttendanceListUseCase,
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
    private val attendanceTypeMapper: AttendanceTypeMapper,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<SessionDetailUiState, SessionDetailUiSideEffect, SessionDetailUiEvent>(SessionDetailUiState()) {

    init {
        setState { copy(loadState = SessionDetailUiState.LoadState.Loading) }
        val sessionId = savedStateHandle.get<Int>("session")
        val currentTime = getCurrentTimeUseCase()

        if (sessionId != null) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    getMemberAttendanceListUseCase().collectLatest { result ->
                        result.onSuccess { (sessions, attendances) ->
                            if (attendances.isEmpty()) {
                                setState { copy(loadState = SessionDetailUiState.LoadState.Error) }
                                return@onSuccess
                            }

                            val session = sessions[sessionId]
                            val attendance = attendances[sessionId]

                            setState {
                                copy(
                                    loadState = SessionDetailUiState.LoadState.Idle,
                                    appBarTitle = session.title,
                                    screenState = SessionDetailScreenState(
                                        title = session.title,
                                        description = session.description,
                                        date = session.monthAndDay,
                                        attendanceType = attendanceTypeMapper.map(
                                            sessionType = session.type,
                                            attendanceStatus = attendance.status,
                                            isPastSession = currentTime.isAfter(session.startTime)
                                        )
                                    )
                                )
                            }
                        }
                            .onFailure {
                                setState { copy(loadState = SessionDetailUiState.LoadState.Error) }
                            }
                    }
                }
            }
        } else {
            setState { copy(loadState = SessionDetailUiState.LoadState.Error) }
        }
    }

    override suspend fun handleEvent(event: SessionDetailUiEvent) = Unit

}
