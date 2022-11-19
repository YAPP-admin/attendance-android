package com.yapp.domain.model.types


sealed class AttendanceType(val point: Int, val text: String) {
    object Absent : AttendanceType(point = -20, text = "결석")
    object Normal : AttendanceType(point = 0, text = "출석")
    object Late : AttendanceType(point = -10, text = "지각")
    object Admit : AttendanceType(point = 0, text = "출석 인정")
}