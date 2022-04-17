package com.yapp.presentation.ui.member.todaysession

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.model.AttendanceType
import com.yapp.presentation.model.Session

class TodaySessionContract {
    data class TodaySessionUiState(
        val loadState: LoadState = LoadState.Idle,
        val sessionId: Int = 0,
        val attendanceType: AttendanceType = AttendanceType.Absent,
        val todaySession: Session? = null,
    ) : UiState

    enum class LoadState {
        Loading, Idle, Error
    }

    sealed class TodaySessionUiSideEffect : UiSideEffect {
    }

    sealed class TodaySessionUiEvent : UiEvent {
        object OnInitializeComposable : TodaySessionUiEvent()
    }
}