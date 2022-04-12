package com.yapp.presentation.ui.admin.totalscore

import com.yapp.common.base.BaseViewModel
import com.yapp.presentation.model.Attendance
import com.yapp.presentation.model.AttendanceType
import com.yapp.presentation.model.Member
import com.yapp.presentation.model.Team
import com.yapp.presentation.model.collections.AttendanceList
import com.yapp.presentation.model.type.PositionType
import com.yapp.presentation.model.type.TeamType
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

    override suspend fun handleEvent(event: AdminTotalScoreUiEvent) {

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
        return when ((0..5).random()) {
            0 -> PositionType.DEV_ANDROID
            1 -> PositionType.DEV_WEB
            2 -> PositionType.DEV_IOS
            3 -> PositionType.DEV_SERVER
            4 -> PositionType.DESIGNER
            else -> PositionType.PROJECT_MANAGER
        }
    }

    private fun getRandomTeam(): Team {
        val platformType = when ((0..3).random()) {
            0 -> "ANDROID"
            1 -> "IOS"
            2 -> "WEB"
            else -> "ALL_ROUNDER"
        }
        val number = (1..2).random()
        return Team(type = TeamType.of(platformType), number = number)
    }

    private fun getRandomAttendanceList(): AttendanceList {
        val scores = mutableListOf<Attendance>()
        for (i in 1..20) {
            scores.add(
                Attendance(
                    sessionId = i,
                    attendanceType = when ((0..50).random()) {
                        0 -> AttendanceType.Absent
                        1 -> AttendanceType.Late
                        2 -> AttendanceType.Admit
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
            .sortedWith(compareBy({ it.type }, { it.number }))) {
            val teamMembers = groupMembers[key]!!.map { member ->
                MemberWithTotalScore(
                    member.name,
                    member.attendances.getTotalAttendanceScore()
                )
            }
            result.add(
                TeamItemState(
                    teamName = "${key.type!!.name} ${key.number}팀",
                    teamMembers = teamMembers
                )
            )
        }
        return result
    }
}