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
import com.yapp.presentation.model.type.TeamType
import com.yapp.presentation.ui.member.signup.team.TeamContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
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
                            platform = PlatformType.of(
                                event.platformType
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

    suspend fun setMember() {
        val name = savedStateHandle.get<String>("name")
        var memberId: Long? = -1

//        getMemberIdUseCase().collectWithCallback(
//            onSuccess = { memberId = it },
//            onFailed = {}
//        )


        setMemberUseCase(
            name?.let { memberName ->
                memberId?.let { memberId ->
                    MemberEntity(
                        id = memberId,
                        name = memberName,
                        position = PositionTypeEntity.FRONTEND,
                        team = TeamEntity(
                            platform = uiState.value.selectedTeam.platform!!.name,
                            number = uiState.value.selectedTeam.number!!
                        ),
                        attendances = listOf(
                            AttendanceEntity(
                                sessionId = 1,
                                type = AttendanceTypeEntity.Absent
                            )
                        )
                    )
                }
            }
        ).collectWithCallback(
            onSuccess = { Log.e("### success", "준영님이 아무거나") },
            onFailed = { Log.e("### fail", "넣어주세욧") }
        )
    }
}