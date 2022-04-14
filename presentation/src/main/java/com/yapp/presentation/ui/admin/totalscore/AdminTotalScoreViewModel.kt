package com.yapp.presentation.ui.admin.totalscore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetAllMemberUseCase
import com.yapp.presentation.model.Member
import com.yapp.presentation.model.Member.Companion.mapTo
import com.yapp.presentation.model.Team
import com.yapp.presentation.ui.admin.totalscore.AdminTotalScoreContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminTotalScoreViewModel @Inject constructor(
    private val getAllMemberUseCase: GetAllMemberUseCase,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<AdminTotalScoreUiState, AdminTotalScoreUiSideEffect, AdminTotalScoreUiEvent>(
    AdminTotalScoreUiState()
) {
    init {
        viewModelScope.launch {
            setState { copy(loadState = AdminTotalScoreUiState.LoadState.Loading) }
            getAllScores()
        }
    }

    override suspend fun handleEvent(event: AdminTotalScoreUiEvent) {

    }

    private suspend fun getAllScores() {
        getAllMemberUseCase().collectWithCallback(
            onSuccess = { memberEntity ->
                val members = memberEntity.map { it.mapTo() }
                val upcomingSessionId = savedStateHandle.get<Int>("upcomingSessionId") ?: -1
                val groupMembers = members.groupBy {
                    it.team
                }
                val teamItemStates = getTeamItemStates(groupMembers, upcomingSessionId)
                setState {
                    copy(
                        loadState = AdminTotalScoreUiState.LoadState.Idle,
                        teamItemStates = teamItemStates
                    )
                }
            },
            onFailed = {
                setState { copy(loadState = AdminTotalScoreUiState.LoadState.Error) }
            }
        )
    }

    private fun getTeamItemStates(
        teamMembersMap: Map<Team, List<Member>>,
        upcomingSessionId: Int
    ): List<AdminTotalScoreUiState.TeamItemState> {
        return teamMembersMap.map { team ->
            AdminTotalScoreUiState.TeamItemState(
                teamName = "${team.key.type?.displayName} ${team.key.number}팀",
                teamMembers = team.value.map { member ->
                    AdminTotalScoreUiState.MemberWithTotalScore(
                        member.name,
                        member.attendances.getTotalAttendanceScore(upcomingSessionId)
                    )
                }
            )
        }.sortedBy { it.teamName }
    }
}