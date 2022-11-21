package com.yapp.domain.model

import com.yapp.domain.model.collections.AttendanceList
import com.yapp.domain.model.types.PositionType

data class Member(
    val id: Long,
    val name: String,
    val position: PositionType,
    val team: Team,
    val attendances: AttendanceList
)