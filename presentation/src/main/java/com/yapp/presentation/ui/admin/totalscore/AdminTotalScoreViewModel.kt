package com.yapp.presentation.ui.admin.totalscore

import com.yapp.common.base.BaseViewModel
import com.yapp.presentation.ui.admin.totalscore.AdminTotalScoreContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminTotalScoreViewModel @Inject constructor(

) : BaseViewModel<AdminTotalScoreUiState, AdminTotalScoreUiSideEffect, AdminTotalScoreUiEvent>(
    AdminTotalScoreUiState()
) {
    init {
        getAllScores()
    }

    override fun handleEvent(event: AdminTotalScoreUiEvent) {
        when (event) {
            is AdminTotalScoreUiEvent.ClickTeamItem -> changeIsExpandedState(event.index)
        }
    }

    private fun changeIsExpandedState(index: Int) {
        val newTeamItemStates = mutableListOf<TeamItemState>()
        uiState.value.teamItemStates.forEachIndexed { i, teamItemState ->
            if (index == i) {
                newTeamItemStates.add(
                    TeamItemState(
                        teamName = teamItemState.teamName,
                        teamMembers = teamItemState.teamMembers,
                        isExpanded = !teamItemState.isExpanded
                    )
                )
            } else {
                newTeamItemStates.add(teamItemState)
            }
        }
        setState {
            copy(teamItemStates = newTeamItemStates)
        }
    }

    private fun getAllScores() {
        // todo: DB에서 출결 정보 가져온 뒤 최종 점수 계산 & state 변경
        val member1 = MemberScore("신짱구", 100)
        val member2 = MemberScore("노진구", 80)
        val member3 = MemberScore("흰둥이", 90)
        val member4 = MemberScore("도라에몽", 65)
        val allRounderTeam =
            TeamItemState(false, "All-rounder 1팀", listOf(member1, member2, member3, member4))
        val webTeam = TeamItemState(false, "Web 1팀", listOf(member1, member2, member3, member4))
        val androidTeam =
            TeamItemState(false, "Android 1팀", listOf(member1, member2, member3, member4))
        val iosTeam = TeamItemState(false, "iOS 1팀", listOf(member1, member2, member3, member4))
        setState {
            copy(teamItemStates = listOf(allRounderTeam, webTeam, androidTeam, iosTeam))
        }
    }


}