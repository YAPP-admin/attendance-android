package com.yapp.domain.model


sealed class AttendanceTypeEntity(val point: Int, val text: String) {
    object Absent : AttendanceTypeEntity(point = -20, text = "미통보 결석")
    object ReportedAbsent : AttendanceTypeEntity(point = -10, text = "결석")
    object Normal : AttendanceTypeEntity(point = 0, text = "출석")
    object Late : AttendanceTypeEntity(point = -10, text = "미통보 지각")
    object ReportedLate : AttendanceTypeEntity(point = -5, text = "지각" )
    object Empty : AttendanceTypeEntity(point = 0, text = "EMPTY")
}