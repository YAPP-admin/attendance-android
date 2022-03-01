package com.yapp.presentation.ui.member.signup

import com.google.gson.Gson
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.util.firebase.FirebaseRemoteConfig
import com.yapp.domain.util.firebase.RemoteConfigData
import com.yapp.presentation.model.TeamModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(firebaseRemoteConfig: FirebaseRemoteConfig) :
    BaseViewModel<TeamContract.TeamUiState, TeamContract.TeamSideEffect, TeamContract.TeamUiEvent>(
        TeamContract.TeamUiState()
    ) {

    init {
        firebaseRemoteConfig.getValue(RemoteConfigData.AttendanceSelectTeams) { json ->
            val teams = Gson().fromJson(json, Array<TeamModel>::class.java).toList()
            setState { copy(teams = teams) }
        }

    }

    override fun handleEvent(event: TeamContract.TeamUiEvent) {
        when (event) {
            is TeamContract.TeamUiEvent.ChooseTeam -> {
                setState { copy(myTeam = uiState.value.myTeam.copy(team = event.team)) }
            }
            is TeamContract.TeamUiEvent.ChooseTeamNumber -> {
                setState { copy(myTeam = uiState.value.myTeam.copy(count = event.teamNum)) }
            }
        }
    }
}