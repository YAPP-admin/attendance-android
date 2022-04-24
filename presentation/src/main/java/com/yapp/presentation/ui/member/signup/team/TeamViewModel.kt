package com.yapp.presentation.ui.member.signup.team

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.model.TeamEntity
import com.yapp.domain.model.types.PositionTypeEntity
import com.yapp.domain.usecases.GetTeamListUseCase
import com.yapp.domain.usecases.SignUpMemberUseCase
import com.yapp.presentation.model.Team.Companion.mapTo
import com.yapp.presentation.model.type.TeamType
import com.yapp.presentation.ui.member.signup.team.TeamContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val getTeamListUseCase: GetTeamListUseCase,
    private val signUpMemberUseCase: SignUpMemberUseCase,
    private val savedStateHandle: SavedStateHandle,
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

    override suspend fun handleEvent(event: TeamUiEvent) {
        when (event) {
            is TeamUiEvent.ChooseTeam -> {
                setState {
                    copy(
                        selectedTeam = uiState.value.selectedTeam.copy(
                            type = TeamType.of(
                                event.teamType
                            )
                        )
                    )
                }
            }
            is TeamUiEvent.ChooseTeamNumber -> {
                setState { copy(selectedTeam = uiState.value.selectedTeam.copy(number = event.teamNum)) }
            }
            is TeamUiEvent.ConfirmTeam -> {
                if(savedStateHandle.get<String>("name") == null || savedStateHandle.get<String>("position") == null) {
                    setEffect(TeamSideEffect.ShowToast("회원가입 실패"))
                    return
                }

                signUpMember(
                    memberName = savedStateHandle.get<String>("name")!!,
                    memberPosition = PositionTypeEntity.of(savedStateHandle.get<String>("position")!!),
                    teamEntity = TeamEntity(
                        type = uiState.value.selectedTeam.type!!.name,
                        number = uiState.value.selectedTeam.number!!
                    )
                )
            }
        }
    }

    private suspend fun signUpMember(
        memberName: String,
        memberPosition: PositionTypeEntity,
        teamEntity: TeamEntity
    ) {
        signUpMemberUseCase(
            params = SignUpMemberUseCase.Params(
                memberName = memberName,
                memberPosition = memberPosition,
                teamEntity = teamEntity
            )
        ).collectWithCallback(
            onSuccess = {
                setEffect(TeamSideEffect.NavigateToMainScreen)
            },
            onFailed = {
                setEffect(TeamSideEffect.ShowToast("회원가입 실패"))
            }
        )
    }

}