package com.yapp.domain.model

data class AttendanceEntity(
    val sessionId: Int,
    val type: AttendanceTypeEntity
) {
    companion object {
        const val MAX_SESSION = 20
    }
}

sealed class AttendanceTypeEntity(val point: Int, val text: String) {
    object Absent : AttendanceTypeEntity(point = -20, text = "결석")
    object Normal : AttendanceTypeEntity(point = 0, text = "출석")
    object Late : AttendanceTypeEntity(point = -10, text = "지각")
    object Admit : AttendanceTypeEntity(point = 0, text = "출석 인정")
}