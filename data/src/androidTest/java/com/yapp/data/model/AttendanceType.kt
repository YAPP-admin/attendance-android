package com.yapp.data.model


internal enum class AttendanceType(val point: Int, val text: String) {
    Absent(point = -20, text = "미통보 결석"),
    ReportedAbsent(point = -10, text = "결석"),
    Normal(point = 0, text = "출석"),
    Late(point = -10, text = "미통보 지각"),
    ReportedLate(point = -5, text = "지각" )
}