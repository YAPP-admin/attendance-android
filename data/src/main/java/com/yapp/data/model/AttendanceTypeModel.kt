package com.yapp.data.model

import com.yapp.domain.model.AttendanceTypeEntity


sealed class AttendanceTypeModel(val point: Int, val text: String) {
    object Absent : AttendanceTypeModel(point = -20, text = "미통보 결석")
    object ReportedAbsent : AttendanceTypeModel(point = -10, text = "결석")
    object Normal : AttendanceTypeModel(point = 0, text = "출석")
    object Late : AttendanceTypeModel(point = -10, text = "미통보 지각")
    object ReportedLate : AttendanceTypeModel(point = -5, text = "지각")
    object Empty : AttendanceTypeModel(point = 0, text = "EMPTY")

    companion object {
        fun AttendanceTypeModel.toEntity(): AttendanceTypeEntity {
            return when (this) {
                is Absent -> AttendanceTypeEntity.Absent
                is ReportedAbsent -> AttendanceTypeEntity.ReportedAbsent
                is Normal -> AttendanceTypeEntity.Normal
                is Late -> AttendanceTypeEntity.Late
                is ReportedLate -> AttendanceTypeEntity.ReportedLate
                is Empty -> AttendanceTypeEntity.Empty
            }
        }
    }
}