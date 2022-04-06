package com.yapp.presentation.model

import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.model.AttendanceTypeEntity
import com.yapp.presentation.model.AttendanceType.Companion.mapTo


data class Attendance(
    val sessionId: Int,
    val attendanceType: AttendanceType
) {

    companion object {
        fun AttendanceEntity.mapTo(): Attendance {
            return Attendance(
                sessionId = this.sessionId,
                attendanceType = this.type.mapTo()
            )
        }
    }

}

sealed class AttendanceType(val point: Int, val text: String) {

    companion object {
        fun AttendanceTypeEntity.mapTo(): AttendanceType {
            return when (this) {
                is AttendanceTypeEntity.Absent -> AttendanceType.Absent
                is AttendanceTypeEntity.Normal -> AttendanceType.Normal
                is AttendanceTypeEntity.Late -> AttendanceType.Late
                is AttendanceTypeEntity.Admit -> AttendanceType.Admit
            }
        }

        fun getAllTypes() : List<AttendanceType> {
            return listOf(Absent, ReportedAbsent, Normal, Late, ReportedLate)
        }
    }

    object Absent : AttendanceType(point = -20, text = "결석")
    object Normal : AttendanceType(point = 0, text = "출석")
    object Late : AttendanceType(point = -10, text = "지각")
    object Admit : AttendanceType(point = 0, text = "출석 인정")
}