package com.yapp.presentation.ui.member.signup.team

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetTeamListUseCase
import com.yapp.presentation.model.Team.Companion.mapTo
import com.yapp.presentation.model.type.TeamType
import com.yapp.presentation.ui.member.signup.team.TeamContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor (
    private val getTeamListUseCase: GetTeamListUseCase
) : BaseViewModel<TeamUiState, TeamSideEffect, TeamUiEvent>(TeamUiState()) {

    init {
        viewModelScope.launch {
            getTeamListUseCase()
                .collectWithCallback(
                    onSuccess = { teamEntities ->
                        setState { copy(teams = teamEntities.map { it.mapTo() }) }
                    },
                    onFailed = {
                        //에러 핸들링 필요합니다
                    }
                )
        }

    }

    override fun handleEvent(event: TeamUiEvent) {
        when (event) {
            is TeamUiEvent.ChooseTeam -> {
                setState { copy(selectedTeam = uiState.value.selectedTeam.copy(type = TeamType.of(event.platformType))) }
            }
            is TeamUiEvent.ChooseTeamNumber -> {
                setState { copy(selectedTeam = uiState.value.selectedTeam.copy(number = event.teamNum)) }
            }
        }
    }

}