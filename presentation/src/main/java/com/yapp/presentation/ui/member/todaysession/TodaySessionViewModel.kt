package com.yapp.presentation.ui.member.todaysession

import com.yapp.common.base.BaseViewModel
import com.yapp.domain.model.Attendance
import com.yapp.domain.model.types.VersionType
import com.yapp.domain.usecases.CheckVersionUpdateUseCase
import com.yapp.domain.usecases.GetMemberAttendancesUseCase
import com.yapp.domain.usecases.GetUpcomingSessionUseCase
import com.yapp.presentation.common.AttendanceBundle
import com.yapp.presentation.ui.member.todaysession.TodaySessionContract.DialogState.NECESSARY_UPDATE
import com.yapp.presentation.ui.member.todaysession.TodaySessionContract.DialogState.NONE
import com.yapp.presentation.ui.member.todaysession.TodaySessionContract.DialogState.REQUIRE_UPDATE
import com.yapp.presentation.ui.member.todaysession.TodaySessionContract.LoadState
import com.yapp.presentation.ui.member.todaysession.TodaySessionContract.TodaySessionUiEvent
import com.yapp.presentation.ui.member.todaysession.TodaySessionContract.TodaySessionUiSideEffect
import com.yapp.presentation.ui.member.todaysession.TodaySessionContract.TodaySessionUiState
import com.yapp.presentation.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

@HiltViewModel
class TodaySessionViewModel @Inject constructor(
    private val checkVersionUpdateUseCase: CheckVersionUpdateUseCase,
    private val getUpcomingSessionUseCase: GetUpcomingSessionUseCase,
    private val getMemberAttendancesUseCase: GetMemberAttendancesUseCase,
    private val resourceProvider: ResourceProvider,
) : BaseViewModel<TodaySessionUiState, TodaySessionUiSideEffect, TodaySessionUiEvent>(
    TodaySessionUiState()
) {

    override suspend fun handleEvent(event: TodaySessionUiEvent) {
        when (event) {
            is TodaySessionUiEvent.OnInitializeComposable -> {
                checkRequireVersionUpdate()
            }

            is TodaySessionUiEvent.OnUpdateButtonClicked -> {
                setEffect(
                    TodaySessionUiSideEffect.NavigateToPlayStore
                )

                // 강제 업데이트가 필요하지 않은 경우 '업데이트' 버튼 클릭 시 다이얼로그를 닫는다.
                if (currentState.dialogState == NECESSARY_UPDATE) {
                    setState { copy(dialogState = NONE) }
                }
            }

            is TodaySessionUiEvent.OnCancelButtonClicked -> {
                setState { copy(dialogState = NONE) }
            }
        }
    }

    private suspend fun checkRequireVersionUpdate() {
        checkVersionUpdateUseCase(resourceProvider.getVersionCode())
            .onSuccess { versionType ->
                when (versionType) {
                    VersionType.NOT_REQUIRED -> Unit
                    VersionType.REQUIRED -> setState { copy(dialogState = REQUIRE_UPDATE) }
                    VersionType.UPDATED_BUT_NOT_REQUIRED -> setState { copy(dialogState = NECESSARY_UPDATE) }
                }

                getUpcomingSession()
            }
            .onFailure {
                // TODO : 버전 로드 실패
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


