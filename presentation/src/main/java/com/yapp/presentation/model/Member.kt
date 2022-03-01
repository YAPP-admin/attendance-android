package com.yapp.presentation.model

import com.yapp.domain.model.ConfigEntity
import com.yapp.domain.model.MemberEntity
import com.yapp.presentation.model.AttendanceType.Companion.mapTo
import com.yapp.presentation.model.collections.AttendanceList


data class Member(
    val id: Long,
    val name: String,
    val position: String,
    val team: String,
    val isAdmin: Boolean,
    val attendances: AttendanceList
) {
    companion object {
        fun MemberEntity.mapTo(): Member {
            return Member(
                id = id,
                name = name,
                position = position,
                team = team,
                isAdmin = isAdmin,
                attendances = AttendanceList.of(attendances.map { it.mapTo() })
            )
        }
    }
}