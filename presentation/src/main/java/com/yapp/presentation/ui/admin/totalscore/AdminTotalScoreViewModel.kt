package com.yapp.presentation.ui.admin.totalscore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetAllMemberUseCase
import com.yapp.presentation.model.Member
import com.yapp.presentation.model.Member.Companion.mapTo
import com.yapp.presentation.model.Team
import com.yapp.presentation.model.collections.AttendanceList
import com.yapp.presentation.ui.admin.AdminConstants.KEY_LAST_SESSION_ID
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
        when (event) {
            is AdminTotalScoreUiEvent.OnBackArrowClick -> setEffect(AdminTotalScoreUiSideEffect.NavigateToPreviousScreen)
        }
    }

    private suspend fun getAllScores() {
        getAllMemberUseCase().collectWithCallback(
            onSuccess = { memberEntity ->
                val members = memberEntity.map { it.mapTo() }.groupBy { it.team }
                val lastSessionId = savedStateHandle.get<Int>(KEY_LAST_SESSION_ID)
                    ?: AttendanceList.DEFAULT_UPCOMING_SESSION_ID
                val teamItemStates = getTeamItemStates(members, lastSessionId)
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
        lastSessionId: Int
    ): List<AdminTotalScoreUiState.TeamItemState> {
        return teamMembersMap.map { team ->
            AdminTotalScoreUiState.TeamItemState(
                teamName = "${team.key.type?.displayName} ${team.key.number}???",
                teamMembers = team.value.map { member ->
                    AdminTotalScoreUiState.MemberWithTotalScore(
                        member.name,
                        member.attendances.getTotalAttendanceScore(lastSessionId)
                    )
                }
            )
        }.sortedBy { it.teamName }
    }
}