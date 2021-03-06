package com.yapp.presentation.ui.member.score

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetMemberAttendanceListUseCase
import com.yapp.domain.util.DateUtil
import com.yapp.presentation.model.Attendance.Companion.mapTo
import com.yapp.presentation.model.Session.Companion.mapTo
import com.yapp.presentation.ui.member.score.MemberScoreContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MemberScoreViewModel @Inject constructor(
    private val getMemberAttendanceListUseCase: GetMemberAttendanceListUseCase,

    ) : BaseViewModel<MemberScoreUiState, MemberScoreUiSideEffect, MemberScoreUiEvent>(
    MemberScoreUiState()
) {
    init {

    }

    private suspend fun fetchMemberScore() = viewModelScope.launch {
        setState { copy(loadState = MemberScoreUiState.LoadState.Loading) }
        withContext(Dispatchers.IO) {
            getMemberAttendanceListUseCase()
                .collectWithCallback(
                    onSuccess = { entities ->
                        val session = entities.first.map { entity -> entity.mapTo() }
                        val attendance = entities.second?.map { entity -> entity.mapTo() }
                        if (!attendance.isNullOrEmpty()) {
                            val attendanceList = session zip attendance
                            setState {
                                copy(
                                    loadState = MemberScoreUiState.LoadState.Idle,
                                    attendanceList = attendanceList,
                                    lastAttendanceList = attendanceList.filter {
                                        DateUtil.isPastSession(
                                            it.first.date
                                        )
                                    }
                                )
                            }
                        } else {
                            setState { copy(loadState = MemberScoreUiState.LoadState.Error) }
                        }
                    },
                    onFailed = {
                        setState { copy(loadState = MemberScoreUiState.LoadState.Error) }
                    }
                )
        }
    }

    override fun setEvent(event: MemberScoreUiEvent) {
        super.setEvent(event)
    }

    override suspend fun handleEvent(event: MemberScoreUiEvent) {
        when(event) {
            is MemberScoreUiEvent.GetMemberScore -> fetchMemberScore()
        }
    }
}