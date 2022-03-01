package com.yapp.presentation.model

import com.yapp.domain.model.AttendanceTypeEntity

sealed class AttendanceType(val point: Int, val text: String) {

    companion object {
        fun AttendanceTypeEntity.mapTo(): AttendanceType {
            return when(this) {
                is AttendanceTypeEntity.Absent -> AttendanceType.Absent
                is AttendanceTypeEntity.ReportedAbsent -> AttendanceType.ReportedAbsent
                is AttendanceTypeEntity.Normal -> AttendanceType.Normal
                is AttendanceTypeEntity.Late -> AttendanceType.Late
                is AttendanceTypeEntity.ReportedLate -> AttendanceType.ReportedLate
                is AttendanceTypeEntity.Empty -> AttendanceType.Empty
            }
        }
    }

    object Absent : AttendanceType(point = -20, text = "미통보 결석")
    object ReportedAbsent : AttendanceType(point = -10, text = "결석")
    object Normal : AttendanceType(point = 0, text = "출석")
    object Late : AttendanceType(point = -10, text = "미통보 지각")
    object ReportedLate : AttendanceType(point = -5, text = "지각" )
    object Empty : AttendanceType(point = 0, text = "EMPTY")

}