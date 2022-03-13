package com.yapp.domain.model

import com.yapp.domain.model.types.PositionTypeEntity

data class MemberEntity(
    val id: Long,
    val name: String,
    val position: PositionTypeEntity,
    val team: TeamEntity,
    val isAdmin: Boolean,
    val attendances: List<AttendanceEntity>
)