package com.yapp.presentation.ui.member.score

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.model.Session

class MemberScoreContract {
    data class MemberScoreUiState(
        val isLoading: Boolean = true,
        val sessions: List<Session> = emptyList(),
    ) : UiState

    sealed class MemberScoreUiSideEffect : UiSideEffect {
    }

    sealed class MemberScoreUiEvent : UiEvent {
    }
}
