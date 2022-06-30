package com.yapp.presentation.ui.admin.main

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.model.Session
import com.yapp.presentation.model.collections.AttendanceList

class AdminMainContract {
    data class AdminMainUiState(
        val loadState: LoadState = LoadState.Idle,
        val upcomingSession: Session? = null,
        val lastSessionId: Int = AttendanceList.DEFAULT_UPCOMING_SESSION_ID,
        val sessions: List<Session> = emptyList()
    ) : UiState {
        enum class LoadState {
            Loading, Idle, Error
        }
    }

    sealed class AdminMainUiSideEffect : UiSideEffect {
        data class NavigateToAdminTotalScore(val lastSessionId: Int) : AdminMainUiSideEffect()
        data class NavigateToManagement(val sessionId: Int, val sessionTitle: String) : AdminMainUiSideEffect()
        object NavigateToLogin: AdminMainUiSideEffect()
    }

    sealed class AdminMainUiEvent : UiEvent {
        data class OnUserScoreCardClicked(val lastSessionId: Int) : AdminMainUiEvent()
        data class OnSessionClicked(val sessionId: Int, val sessionTitle: String) : AdminMainUiEvent()
        object OnLogoutClicked: AdminMainUiEvent()
    }
}
