package com.yapp.presentation.ui.member.signup

import com.yapp.common.base.BaseViewModel
import com.yapp.domain.firebase.FirebaseRemoteConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(firebaseRemoteConfig: FirebaseRemoteConfig) :
    BaseViewModel<TeamContract.TeamUiState, TeamContract.TeamSideEffect, TeamContract.TeamUiEvent>(
        TeamContract.TeamUiState()
    ) {

    init {

    }

    override fun handleEvent(event: TeamContract.TeamUiEvent) {
        when (event) {
            is TeamContract.TeamUiEvent.ChooseTeam -> {
                setState { copy(selectedTeam = uiState.value.selectedTeam.copy(teamType = event.teamType)) }
            }
            is TeamContract.TeamUiEvent.ChooseTeamNumber -> {
                setState { copy(selectedTeam = uiState.value.selectedTeam.copy(teamNum = event.teamNum)) }
            }
        }
    }
}