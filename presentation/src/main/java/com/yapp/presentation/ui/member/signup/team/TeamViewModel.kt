package com.yapp.presentation.ui.member.signup.team

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.model.AttendanceTypeEntity
import com.yapp.domain.model.MemberEntity
import com.yapp.domain.model.TeamEntity
import com.yapp.domain.model.types.PositionTypeEntity
import com.yapp.domain.usecases.GetMemberIdUseCase
import com.yapp.domain.usecases.GetTeamListUseCase
import com.yapp.domain.usecases.SetMemberUseCase
import com.yapp.presentation.model.Team.Companion.mapTo
import com.yapp.presentation.model.type.PositionType
import com.yapp.presentation.model.type.TeamType
import com.yapp.presentation.ui.member.signup.team.TeamContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val getTeamListUseCase: GetTeamListUseCase,
    private val setMemberUseCase: SetMemberUseCase,
    private val getMemberIdUseCase: GetMemberIdUseCase,
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

    override fun handleEvent(event: TeamUiEvent) {
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
                viewModelScope.launch {
                    setMember()
                }
            }
        }
    }

    private suspend fun setMember() {
        val memberName = savedStateHandle.get<String>("name")

        getMemberIdUseCase().collectWithCallback(
            onSuccess = { memberId ->
                if ((memberId == null) or (memberName == null)) {
                    setEffect(TeamSideEffect.ShowToast(memberId.toString()))
                } else {
                    setMemberToFireBase(memberName!!, memberId!!)
                }
            },
            onFailed = { setEffect(TeamSideEffect.ShowToast("멤버 번호 문제")) }
        )
    }

    private suspend fun setMemberToFireBase(memberName: String, memberId: Long) {

        setMemberUseCase(
            MemberEntity(
                id = memberId,
                name = memberName,
                position = PositionTypeEntity.DESIGNER,
                team = TeamEntity(
                    type = uiState.value.selectedTeam.type!!.name,
                    number = uiState.value.selectedTeam.number!!
                ),
                attendances = Array(20) { sessionNum ->
                    AttendanceEntity(
                        sessionId = sessionNum,
                        type = AttendanceTypeEntity.Absent
                    )
                }.toList(),
            )
        ).collectWithCallback(
            onSuccess = { setEffect(TeamSideEffect.NavigateToMainScreen) },
            onFailed = { setEffect(TeamSideEffect.ShowToast("파이어베이스 문제")) }
        )
    }
}