package com.yapp.presentation.ui.admin.totalscore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.model.Member
import com.yapp.domain.model.Team
import com.yapp.domain.model.collections.AttendanceList
import com.yapp.domain.usecases.GetAllMemberUseCase
import com.yapp.presentation.ui.admin.AdminConstants.KEY_LAST_SESSION_ID
import com.yapp.presentation.ui.admin.totalscore.AdminTotalScoreContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
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
        when (event) {
            is AdminTotalScoreUiEvent.OnBackArrowClick -> setEffect(AdminTotalScoreUiSideEffect.NavigateToPreviousScreen)
        }
    }

    private suspend fun getAllScores() {
        getAllMemberUseCase().collect { result ->
            result.onSuccess { members ->
                val memberByTeam = members.groupBy { it.team }

                val lastSessionId = savedStateHandle.get<Int>(KEY_LAST_SESSION_ID)
                    ?: AttendanceList.DEFAULT_UPCOMING_SESSION_ID
                val teamItemStates = getTeamItemStates(memberByTeam, lastSessionId)
                setState {
                    copy(
                        loadState = AdminTotalScoreUiState.LoadState.Idle,
                        teamItemStates = teamItemStates
                    )
                }
            }.onFailure {
                setState { copy(loadState = AdminTotalScoreUiState.LoadState.Error) }
            }
        }
    }

    private fun getTeamItemStates(
        teamMembersMap: Map<Team, List<Member>>,
        lastSessionId: Int,
    ): List<AdminTotalScoreUiState.TeamItemState> {
        return teamMembersMap.map { team ->
            AdminTotalScoreUiState.TeamItemState(
                teamName = "${team.key.type.value} ${team.key.number}팀",
                teamMembers = team.value.sortedWith(
                    compareBy<Member> { it.attendances.getTotalAttendanceScore(lastSessionId) }
                        .thenBy { it.name }
                ).map { member ->
                    AdminTotalScoreUiState.MemberWithTotalScore(
                        member.name,
                        member.attendances.getTotalAttendanceScore(lastSessionId)
                    )
                }
            )
        }.sortedBy { it.teamName }
    }
}
