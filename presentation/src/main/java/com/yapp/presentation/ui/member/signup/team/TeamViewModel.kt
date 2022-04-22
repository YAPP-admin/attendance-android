package com.yapp.presentation.ui.member.signup.team

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.model.AttendanceTypeEntity
import com.yapp.domain.model.MemberEntity
import com.yapp.domain.model.TeamEntity
import com.yapp.domain.model.types.NeedToAttendType
import com.yapp.domain.model.types.PositionTypeEntity
import com.yapp.domain.usecases.*
import com.yapp.presentation.model.Team.Companion.mapTo
import com.yapp.presentation.model.type.PositionType
import com.yapp.presentation.model.type.TeamType
import com.yapp.presentation.ui.member.signup.team.TeamContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val getSessionListUseCase: GetSessionListUseCase,
    private val getTeamListUseCase: GetTeamListUseCase,
    private val setMemberUseCase: SetMemberUseCase,
    private val getMemberIdUseCase: GetMemberIdUseCase,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<TeamUiState, TeamSideEffect, TeamUiEvent>(TeamUiState()) {
    private var defaultAttendance: List<AttendanceEntity>? = null

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
                viewModelScope.launch {
                    createDefaultAttendance()
                }
            }
        }
    }

    private suspend fun setMember() {
        val memberName = savedStateHandle.get<String>("name")
        val memberPosition = savedStateHandle.get<String>("position")

        getMemberIdUseCase().collectWithCallback(
            onSuccess = { memberId ->
                if ((memberId == null) or (memberName == null) or (memberPosition == null) or (defaultAttendance == null)) {
                    setEffect(TeamSideEffect.ShowToast("회원가입 실패"))
                } else {
                    setMemberToFireBase(memberName!!, memberPosition!!, memberId!!)
                }
            },
            onFailed = { setEffect(TeamSideEffect.ShowToast("회원가입 실패")) }
        )
    }

    private suspend fun setMemberToFireBase(
        memberName: String,
        memberPosition: String,
        memberId: Long
    ) {

        setMemberUseCase(
            MemberEntity(
                id = memberId,
                name = memberName,
                position = PositionTypeEntity.of(memberPosition),
                team = TeamEntity(
                    type = uiState.value.selectedTeam.type!!.name,
                    number = uiState.value.selectedTeam.number!!
                ),
                attendances = defaultAttendance!!
            )
        ).collectWithCallback(
            onSuccess = { setEffect(TeamSideEffect.NavigateToMainScreen) },
            onFailed = { setEffect(TeamSideEffect.ShowToast("회원가입 실패")) }
        )
    }

    private suspend fun createDefaultAttendance() {
        getSessionListUseCase().collectWithCallback(
            onSuccess = { sessionList ->
                 defaultAttendance = sessionList.map { session ->
                    AttendanceEntity(
                        sessionId = session.sessionId,
                        type = when(session.type) {
                            NeedToAttendType.DONT_NEED_ATTENDANCE, NeedToAttendType.DAY_OFF -> AttendanceTypeEntity.Normal
                            NeedToAttendType.NEED_ATTENDANCE -> AttendanceTypeEntity.Absent
                        }
                    )
                }
                setMember()
            },
            onFailed = {}
        )
    }
}