package com.yapp.presentation.ui.member.todaysession

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.domain.model.Attendance
import com.yapp.domain.model.Session

class TodaySessionContract {
    data class TodaySessionUiState(
        val loadState: LoadState = LoadState.Loading,
        val sessionId: Int = 0,
        val attendanceType: Attendance.Status = Attendance.Status.ABSENT,
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
