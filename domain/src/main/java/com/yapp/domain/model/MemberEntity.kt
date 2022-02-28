package com.yapp.domain.model

class MemberEntity(
    val id: Long?,
    val name: String?,
    val position: String?,
    val team: String?,
    val isAdmin: Boolean? = false,
    val attendances: List<AttendanceTypeEntity>?
)