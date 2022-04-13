package com.yapp.presentation.ui.member.todaysession

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetMemberAttendancesUseCase
import com.yapp.domain.usecases.GetUpcomingSessionUseCase
import com.yapp.presentation.model.Attendance.Companion.mapTo
import com.yapp.presentation.model.AttendanceType
import com.yapp.presentation.model.Session.Companion.mapTo
import com.yapp.presentation.ui.member.todaysession.TodaySessionContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodaySessionViewModel @Inject constructor(
    private val getUpcomingSessionUseCase: GetUpcomingSessionUseCase,
    private val getMemberAttendancesUseCase: GetMemberAttendancesUseCase,
) : BaseViewModel<TodaySessionUiState, TodaySessionUiSideEffect, TodaySessionUiEvent>(
    TodaySessionUiState()
) {

    init {
        viewModelScope.launch {
            getUpcomingSessionUseCase()
                .collectWithCallback(
                    onSuccess = { entity ->
                        val session = entity?.mapTo()
                        val sessionId = session?.sessionId ?: 0
                        setState {
                            copy(
                                sessionId = sessionId,
                                todaySession = session
                            )
                        }

                        // 세션 출석 정보 조회
                        getMemberAttendances()
                    },
                    onFailed = {
                        // TODO 엠티뷰 보여주기
                    }
                )
        }
    }

    fun getMemberAttendances() {
        viewModelScope.launch {
            getMemberAttendancesUseCase().collectWithCallback(
                onSuccess = { attendances ->
                    val attendance = attendances?.get(uiState.value.sessionId)?.mapTo()
                    setState {
                        copy(
                            isLoading = false,
                            attendanceType = attendance?.attendanceType ?: AttendanceType.Absent
                        )
                    }
                },
                onFailed = {
                    //TODO
                })
        }
    }

    override fun setEvent(event: TodaySessionUiEvent) {
        super.setEvent(event)
    }

    override suspend fun handleEvent(event: TodaySessionUiEvent) {
        TODO("Not yet implemented")
    }
}