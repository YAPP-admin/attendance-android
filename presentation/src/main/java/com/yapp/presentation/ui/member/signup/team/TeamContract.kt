package com.yapp.presentation.ui.member.signup.team

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.model.Team
import com.yapp.presentation.model.type.TeamType

sealed class TeamContract {
    data class TeamUiState(
        val loadState: LoadState = LoadState.Loading,
        val teams: List<Team> = emptyList(),
        val selectedTeamType: TeamType? = null,
        val selectedTeamNumber: Int? = null,
        val numberOfSelectedTeamType: Int? = null,
    ) : UiState {
        enum class LoadState {
            Loading, Idle, Error
        }
    }

    sealed class TeamSideEffect : UiSideEffect {
        object NavigateToMainScreen : TeamSideEffect()
        data class ShowToast(val msg: String) : TeamSideEffect()
    }

    sealed class TeamUiEvent : UiEvent {
        data class ChooseTeam(val teamType: String) : TeamUiEvent()
        data class ChooseTeamNumber(val teamNum: Int) : TeamUiEvent()
        object ConfirmTeam : TeamUiEvent()
    }
}
