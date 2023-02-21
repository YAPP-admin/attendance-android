package com.yapp.data.model

import com.google.firebase.firestore.PropertyName
import com.yapp.data.model.types.PositionTypeEntity
import com.yapp.data.model.types.toData
import com.yapp.data.model.types.toDomain
import com.yapp.domain.model.Member
import com.yapp.domain.model.collections.AttendanceList

data class MemberEntity(
    @PropertyName("id")
    val id: Long? = null,
    @PropertyName("name")
    val name: String? = null,
    @PropertyName("position")
    val position: PositionTypeEntity? = null,
    @PropertyName("team")
    val team: TeamEntity? = null,
    @PropertyName("attendances")
    val attendances: List<AttendanceEntity>? = null,
)

fun MemberEntity.toDomain(): Member {
    val attendanceList = AttendanceList.from(attendances?.map { it.toDomain() }!!)

    return Member(
        id = id!!,
        name = name!!,
        position = position!!.toDomain(),
        team = team!!.toDomain(),
        attendances = attendanceList
    )
}

fun Member.toData(): MemberEntity {
    return MemberEntity(
        id = id,
        name = name,
        position = position.toData(),
        team = team.toData(),
        attendances = attendances.map { it.toData() }
    )
}
