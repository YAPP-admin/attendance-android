package com.yapp.presentation.ui.member.signup

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.model.TeamModel

class TeamContract {
    data class TeamUiState(
        val isLoading: Boolean = false,
        val teams: List<TeamModel> = emptyList()
    ) : UiState

    sealed class TeamSideEffect : UiSideEffect {}
    sealed class TeamUiEvent : UiEvent {}
}