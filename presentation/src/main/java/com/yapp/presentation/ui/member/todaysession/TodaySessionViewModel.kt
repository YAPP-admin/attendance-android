package com.yapp.presentation.ui.member.todaysession

import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetMemberAttendancesUseCase
import com.yapp.domain.usecases.GetUpcomingSessionUseCase
import com.yapp.presentation.common.AttendanceBundle
import com.yapp.presentation.model.Attendance.Companion.mapTo
import com.yapp.presentation.model.AttendanceType
import com.yapp.presentation.model.Session.Companion.mapTo
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
        getMemberAttendancesUseCase().collectWithCallback(
            onSuccess = { attendances ->
                val attendance =
                    attendances?.first { it.sessionId == uiState.value.sessionId }?.mapTo()
                val attendanceType = attendance?.attendanceType ?: AttendanceType.Absent
                AttendanceBundle.isAbsent = attendanceType is AttendanceType.Absent
                setState {
                    copy(
                        loadState = LoadState.Idle,
                        attendanceType = attendanceType
                    )
                }
            },
            onFailed = {
                setState { this.copy(loadState = LoadState.Error) }
            })
    }

    private suspend fun getUpcomingSession() = coroutineScope {
        setState { this.copy(loadState = LoadState.Loading) }
        getUpcomingSessionUseCase()
            .collectWithCallback(
                onSuccess = { entity ->
                    val session = entity?.mapTo()
                    AttendanceBundle.upComingSession = session
                    setState {
                        copy(
                            sessionId = session?.sessionId ?: 0,
                            todaySession = session
                        )
                    }

                    // 세션 출석 정보 조회
                    getMemberAttendances()
                },
                onFailed = {
                    setState { this.copy(loadState = LoadState.Error) }
                }
            )
    }
}


