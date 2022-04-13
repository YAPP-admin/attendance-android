package com.yapp.presentation.ui.admin.main

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.model.Session

class AdminMainContract {
    data class AdminMainUiState(
        val loadState: LoadState = LoadState.Idle,
        val upcomingSession: Session? = null,
        val sessions: List<Session> = emptyList()
    ) : UiState {
        enum class LoadState {
            Loading, Idle, Error
        }
    }

    sealed class AdminMainUiSideEffect : UiSideEffect {
    }

    sealed class AdminMainUiEvent : UiEvent {
    }
}
