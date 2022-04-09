package com.yapp.presentation.ui.member.todaysession

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.model.Session

class TodaySessionContract {
    data class TodaySessionUiState(
        val isLoading: Boolean = true,
        val todaySession: Session? = null,
    ) : UiState

    sealed class TodaySessionUiSideEffect : UiSideEffect {
    }

    sealed class TodaySessionUiEvent : UiEvent {
    }
}