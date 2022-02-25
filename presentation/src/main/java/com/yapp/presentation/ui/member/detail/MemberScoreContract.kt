package com.yapp.presentation.ui.member.detail

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.model.SessionModel

class MemberScoreContract {
    data class MemberScoreUiState(
        val isLoading: Boolean = true,
        val sessions: List<SessionModel> = emptyList(),
    ) : UiState

    sealed class MemberScoreUiSideEffect : UiSideEffect {
    }

    sealed class MemberScoreUiEvent : UiEvent {
    }
}
