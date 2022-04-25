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
        data class NavigateToAdminTotalScore(val upcomingSessionId: Int) : AdminMainUiSideEffect()
        data class NavigateToManagement(val sessionId: Int, val sessionTitle: String) : AdminMainUiSideEffect()
        object NavigateToLogin: AdminMainUiSideEffect()
    }

    sealed class AdminMainUiEvent : UiEvent {
        data class OnUserScoreCardClicked(val upcomingSessionId: Int) : AdminMainUiEvent()
        data class OnSessionClicked(val sessionId: Int, val sessionTitle: String) : AdminMainUiEvent()
        object OnLogoutClicked: AdminMainUiEvent()
    }
}
