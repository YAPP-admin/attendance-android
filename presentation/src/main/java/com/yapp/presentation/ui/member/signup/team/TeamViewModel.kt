package com.yapp.presentation.ui.member.signup.team

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.model.Team
import com.yapp.domain.model.types.TeamType
import com.yapp.domain.usecases.GetCurrentMemberInfoUseCase
import com.yapp.domain.usecases.GetTeamListUseCase
import com.yapp.domain.usecases.SetMemberUseCase
import com.yapp.presentation.ui.member.signup.team.TeamContract.TeamSideEffect
import com.yapp.presentation.ui.member.signup.team.TeamContract.TeamUiEvent
import com.yapp.presentation.ui.member.signup.team.TeamContract.TeamUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val getTeamListUseCase: GetTeamListUseCase,
    private val getCurrentMemberInfoUseCase: GetCurrentMemberInfoUseCase,
    private val setMemberUseCase: SetMemberUseCase,
) : BaseViewModel<TeamUiState, TeamSideEffect, TeamUiEvent>(TeamUiState()) {

    init {
        viewModelScope.launch {
            getTeamListUseCase()
                .onSuccess { teamEntities ->
                    setState { copy(loadState = TeamUiState.LoadState.Idle, teams = teamEntities) }
                }
                .onFailure {
                    setState { copy(loadState = TeamUiState.LoadState.Error) }
                    setEffect(TeamSideEffect.ShowToast("팀 리스트를 불러오는데 실패했습니다."))
                }
        }
    }

    override suspend fun handleEvent(event: TeamUiEvent) {
        when (event) {
            is TeamUiEvent.ChooseTeam -> {
                val selectedTeamType =
                    uiState.value.teamOptionState.items.find { it.value == event.teamType }
                val numberOfSelectedTeamType =
                    uiState.value.teams.find { it.type == selectedTeamType }?.number

                setState {
                    copy(
                        teamOptionState = TeamContract.TeamOptionState(selectedOption = selectedTeamType),
                        numberOfSelectedTeamType = numberOfSelectedTeamType
                    )
                }
            }

            is TeamUiEvent.ChooseTeamNumber -> {
                setState {
                    copy(teamNumberOptionState = TeamContract.TeamNumberOptionState(selectedOption = event.teamNum))
                }
            }

            is TeamUiEvent.ConfirmTeam -> {
                setMember(
                    team = Team(
                        type = uiState.value.teamOptionState.selectedOption ?: TeamType.NONE,
                        number = uiState.value.teamNumberOptionState.selectedOption ?: 0,
                    )
                )
            }
        }
    }

    private suspend fun setMember(team: Team) {
        setState { copy(loadState = TeamUiState.LoadState.Loading) }
        getCurrentMemberInfoUseCase().onSuccess {
            setState { copy(currentMember = it) }
        }.onFailure {
            setState { copy(loadState = TeamUiState.LoadState.Error) }
            return
        }
        val member = requireNotNull(currentState.currentMember) { "$FAIL_SELECT_TEAM because member is null" }
        setMemberUseCase(
            params = member.copy(team = team)
        ).onSuccess {
            setState { copy(loadState = TeamUiState.LoadState.Idle) }
            setEffect(TeamSideEffect.NavigateToSettingScreen)
        }.onFailure {
            setState { copy(loadState = TeamUiState.LoadState.Error) }
        }
    }

    companion object {
        private const val FAIL_SELECT_TEAM = "회원가입 실패"
    }
}
