package com.yapp.presentation.ui.admin.totalscore

import com.yapp.common.base.BaseViewModel
import com.yapp.presentation.model.Attendance
import com.yapp.presentation.model.AttendanceType
import com.yapp.presentation.model.Member
import com.yapp.presentation.model.Team
import com.yapp.presentation.model.collections.AttendanceList
import com.yapp.presentation.model.type.PlatformType
import com.yapp.presentation.model.type.PositionType
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
        val allMembers = getMockMembers()
        val groupMembers = allMembers.groupBy {
            it.team
        }
        val teamItemStates = getTeamItemStates(groupMembers)

        setState {
            copy(teamItemStates = teamItemStates)
        }
    }

    private fun getMockMembers(): List<Member> {
        return (1..20).map { index ->
            Member(
                id = index.toLong(),
                name = "name$index",
                position = getRandomPosition(),
                team = getRandomTeam(),
                attendances = getRandomAttendanceList()
            )
        }
    }

    private fun getRandomPosition(): PositionType {
        return when ((0..3).random()) {
            0 -> PositionType.FRONTEND
            1 -> PositionType.BACKEND
            2 -> PositionType.DESIGNER
            else -> PositionType.PROJECT_MANAGER
        }
    }

    private fun getRandomTeam(): Team {
        val platformType = when ((0..3).random()) {
            0 -> "Android"
            1 -> "iOS"
            2 -> "Web"
            else -> "All-Rounder"
        }
        val number = (1..2).random()
        return Team(platform = PlatformType.of(platformType), number = number)
    }

    private fun getRandomAttendanceList(): AttendanceList {
        val scores = mutableListOf<Attendance>()
        for (i in 1..20) {
            scores.add(
                Attendance(
                    sessionId = i,
                    attendanceType = when ((0..50).random()) {
                        0 -> AttendanceType.Absent
                        1 -> AttendanceType.ReportedAbsent
                        2 -> AttendanceType.ReportedLate
                        3 -> AttendanceType.Late
                        else -> AttendanceType.Normal
                    }
                )

            )
        }
        return AttendanceList.of(scores)
    }

    private fun getTeamItemStates(groupMembers: Map<Team, List<Member>>): List<TeamItemState> {
        val result = mutableListOf<TeamItemState>()
        for (key in groupMembers.keys.toList()
            .sortedWith(compareBy({ it.platform }, { it.number }))) {
            val teamMembers = groupMembers[key]!!.map { member ->
                MemberWithTotalScore(
                    member.name,
                    member.attendances.getTotalAttendanceScore()
                )
            }
            result.add(
                TeamItemState(
                    isExpanded = false,
                    teamName = "${key.platform!!.name} ${key.number}팀",
                    teamMembers = teamMembers
                )
            )
        }
        return result
    }
}