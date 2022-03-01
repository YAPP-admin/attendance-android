package com.yapp.domain.model

class MemberEntity(
    val id: Long,
    val name: String,
    val position: String,
    val team: String,
    val isAdmin: Boolean,
    val attendances: List<AttendanceTypeEntity>
)