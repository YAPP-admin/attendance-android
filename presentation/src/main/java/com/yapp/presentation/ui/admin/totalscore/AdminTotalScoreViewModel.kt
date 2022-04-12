package com.yapp.presentation.ui.admin.totalscore

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetAllMemberUseCase
import com.yapp.presentation.model.Member
import com.yapp.presentation.model.Member.Companion.mapTo
import com.yapp.presentation.model.Team
import com.yapp.presentation.ui.admin.totalscore.AdminTotalScoreContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminTotalScoreViewModel @Inject constructor(
    private val getAllMemberUseCase: GetAllMemberUseCase,
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
        val allMembers = getMembers()

        if (allMembers != null) {
            val groupMembers = allMembers.groupBy {
                it.team
            }
            val teamItemStates = getTeamItemStates(groupMembers)

            setState {
                copy(
                    loadState = AdminTotalScoreUiState.LoadState.Idle,
                    teamItemStates = teamItemStates
                )
            }
        } else {
            setState {
                copy(loadState = AdminTotalScoreUiState.LoadState.Error)
            }
        }
    }

    private suspend fun getMembers(): List<Member>? = coroutineScope {
        var members: List<Member>? = null
        getAllMemberUseCase().collectWithCallback(
            onSuccess = { memberEntities ->
                members = memberEntities.map { entity -> entity.mapTo() }
            },
            onFailed = {
                members = null
            }
        )
        return@coroutineScope members
    }

    private fun getTeamItemStates(groupMembers: Map<Team, List<Member>>): List<AdminTotalScoreUiState.TeamItemState> {
        val result = mutableListOf<AdminTotalScoreUiState.TeamItemState>()
        for (team in groupMembers.keys.toList()
            .sortedWith(compareBy({ it.type }, { it.number }))) {
            val teamMembers = groupMembers[team]!!.map { member ->
                AdminTotalScoreUiState.MemberWithTotalScore(
                    member.name,
                    member.attendances.getTotalAttendanceScore()
                )
            }
            result.add(
                AdminTotalScoreUiState.TeamItemState(
                    teamName = "${team.type?.displayName} ${team.number}팀",
                    teamMembers = teamMembers
                )
            )
        }
        return result
    }
}