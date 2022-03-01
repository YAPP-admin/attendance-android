package com.yapp.presentation.model


data class Member(
    val id: Long,
    val name: String,
    val position: String,
    val team: String,
    val isAdmin: Boolean,
    val attendances: List<AttendanceType>
)