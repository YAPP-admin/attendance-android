package com.yapp.presentation.ui.member.signup.team

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.common.yds.YDSOptionState
import com.yapp.domain.model.Team
import com.yapp.domain.model.types.TeamType

sealed class TeamContract {
    data class TeamUiState(
        val loadState: LoadState = LoadState.Loading,
        val teams: List<Team> = emptyList(),
        val teamOptionState: YDSOptionState<TeamType> = TeamOptionState(),
        val numberOfSelectedTeamType: Int? = null,
        val teamNumberOptionState: YDSOptionState<Int> = TeamNumberOptionState(),
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

    data class TeamOptionState(
        override val items: List<TeamType> = TeamType.values().toList(),
        override val selectedOption: TeamType? = null
    ) : YDSOptionState<TeamType>

    data class TeamNumberOptionState(
        override val items: List<Int> = emptyList(),
        override val selectedOption: Int? = null
    ) : YDSOptionState<Int>
}
