package com.yapp.data.model


internal class Member(
    val id: String?,
    val name: String?,
    val position: String?,
    val team: String?,
    val isAdmin: Boolean? = false,
    val attendances: List<AttendanceType>?
) {
}