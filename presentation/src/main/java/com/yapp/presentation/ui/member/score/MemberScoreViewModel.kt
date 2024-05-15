package com.yapp.presentation.ui.member.score

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetCurrentTimeUseCase
import com.yapp.domain.usecases.GetMemberAttendanceListUseCase
import com.yapp.presentation.common.AttendanceTypeMapper
import com.yapp.presentation.ui.member.score.MemberScoreContract.MemberScoreUiEvent
import com.yapp.presentation.ui.member.score.MemberScoreContract.MemberScoreUiSideEffect
import com.yapp.presentation.ui.member.score.MemberScoreContract.MemberScoreUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberScoreViewModel @Inject constructor(
    private val getMemberAttendanceListUseCase: GetMemberAttendanceListUseCase,
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
    private val attendanceTypeMapper: AttendanceTypeMapper
) : BaseViewModel<MemberScoreUiState, MemberScoreUiSideEffect, MemberScoreUiEvent>(initialState = MemberScoreUiState()) {

    init {
        viewModelScope.launch { fetchMemberScore() }
    }

    private suspend fun fetchMemberScore() {
        setState { copy(loadState = MemberScoreUiState.LoadState.Loading) }
        getMemberAttendanceListUseCase().collectLatest { result ->
            result.onSuccess { (sessions, attendances) ->
                if (attendances.isEmpty()) {
                    setState { copy(loadState = MemberScoreUiState.LoadState.Error) }
                    return@onSuccess
                }

                val currentTime = getCurrentTimeUseCase()
                val attendanceList = (sessions zip attendances).map { (session, attendance) ->
                    val attendanceType = attendanceTypeMapper.map(
                        sessionType = session.type,
                        attendanceStatus = attendance.status,
                        isPastSession = currentTime.isAfter(session.startTime)
                    )

                    session to attendanceType
                }

                setState {
                    copy(
                        loadState = MemberScoreUiState.LoadState.Idle,
                        attendanceList = attendanceList,
                        lastAttendanceList = (sessions zip attendances).filter { (session, _) ->
                            currentTime.isAfter(session.startTime)
                        }
                    )
                }
            }.onFailure {
                setState { copy(loadState = MemberScoreUiState.LoadState.Error) }
            }
        }
    }

    override suspend fun handleEvent(event: MemberScoreUiEvent) {
        when (event) {
            is MemberScoreUiEvent.GetMemberScore -> fetchMemberScore()
        }
    }
}
