package com.yapp.presentation.ui.member.signup.team

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.model.Team

class TeamContract {
    data class TeamUiState(
        val isLoading: Boolean = false,
        val teams: List<Team> = emptyList(),
        val selectedTeam: Team = Team()
    ) : UiState

    sealed class TeamSideEffect : UiSideEffect {}

    sealed class TeamUiEvent : UiEvent {
        data class ChooseTeam(val platformType: String) : TeamUiEvent()
        data class ChooseTeamNumber(val teamNum: Int) : TeamUiEvent()
        object ConfirmTeam : TeamUiEvent()
    }
}