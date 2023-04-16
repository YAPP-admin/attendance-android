package com.yapp.presentation.ui.member.todaysession

import com.yapp.common.base.BaseViewModel
import com.yapp.domain.model.Attendance
import com.yapp.domain.usecases.GetMemberAttendancesUseCase
import com.yapp.domain.usecases.GetUpcomingSessionUseCase
import com.yapp.presentation.common.AttendanceBundle
import com.yapp.presentation.ui.member.todaysession.TodaySessionContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

@HiltViewModel
class TodaySessionViewModel @Inject constructor(
    private val getUpcomingSessionUseCase: GetUpcomingSessionUseCase,
    private val getMemberAttendancesUseCase: GetMemberAttendancesUseCase,
) : BaseViewModel<TodaySessionUiState, TodaySessionUiSideEffect, TodaySessionUiEvent>(
    TodaySessionUiState()
) {

    override suspend fun handleEvent(event: TodaySessionUiEvent) {
        when (event) {
            is TodaySessionUiEvent.OnInitializeComposable -> {
                getUpcomingSession()
            }
        }
    }

    private suspend fun getMemberAttendances() = coroutineScope {
        setState { this.copy(loadState = LoadState.Loading) }
        getMemberAttendancesUseCase()
            .onSuccess { attendances ->
                val attendance = attendances?.first { it.sessionId == uiState.value.sessionId }
                val attendanceStatus = attendance?.status ?: Attendance.Status.ABSENT

                AttendanceBundle.isAbsent = attendanceStatus == Attendance.Status.ABSENT

                setState {
                    copy(
                        loadState = LoadState.Idle,
                        attendanceType = attendanceStatus
                    )
                }
            }
            .onFailure {
                setState { this.copy(loadState = LoadState.Error) }
            }
    }

    private suspend fun getUpcomingSession() = coroutineScope {
        setState { this.copy(loadState = LoadState.Loading) }
        getUpcomingSessionUseCase()
            .onSuccess { upComingSession ->
                AttendanceBundle.upComingSession = upComingSession

                setState {
                    copy(
                        sessionId = upComingSession?.sessionId ?: 0,
                        todaySession = upComingSession
                    )
                }

                // 세션 출석 정보 조회
                getMemberAttendances()
            }
            .onFailure {
                setState { this.copy(loadState = LoadState.Error) }
            }
    }
}


