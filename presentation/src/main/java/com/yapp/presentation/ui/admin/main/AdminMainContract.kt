package com.yapp.presentation.ui.admin.main

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.model.SessionModel

class AdminMainContract {
    data class AdminMainUiState(
        val isLoading: Boolean = true,
        val sessions: List<SessionModel> = emptyList()
    ) : UiState

    sealed class AdminMainUiSideEffect : UiSideEffect {
    }

    sealed class AdminMainUiEvent : UiEvent {
    }
}
