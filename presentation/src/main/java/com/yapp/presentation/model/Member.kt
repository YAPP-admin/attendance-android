package com.yapp.presentation.model

import com.yapp.domain.model.MemberEntity
import com.yapp.presentation.model.Attendance.Companion.mapTo
import com.yapp.presentation.model.Team.Companion.mapTo
import com.yapp.presentation.model.collections.AttendanceList
import com.yapp.presentation.model.type.PositionType


data class Member(
    val id: Long,
    val name: String,
    val position: PositionType,
    val team: Team,
    val isAdmin: Boolean,
    val attendances: AttendanceList
) {
    companion object {
        fun MemberEntity.mapTo(): Member {
            return Member(
                id = id,
                name = name,
                position = PositionType.valueOf(position.name),
                team = team.mapTo(),
                isAdmin = isAdmin,
                attendances = AttendanceList.of(attendances.map { it.mapTo() })
            )
        }
    }
}