package com.yapp.domain.model.types


sealed class AttendanceType(val point: Int, val text: String, val order: Int) {
    object Absent : AttendanceType(point = -20, text = "결석", order = 1)
    object Late : AttendanceType(point = -10, text = "지각", order = 2)
    object Admit : AttendanceType(point = 0, text = "출석 인정", order = 3)
    object Normal : AttendanceType(point = 0, text = "출석", order = 4)
}
