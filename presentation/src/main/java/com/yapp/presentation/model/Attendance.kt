package com.yapp.presentation.model

import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.model.AttendanceTypeEntity
import com.yapp.presentation.model.AttendanceType.Companion.mapTo
import com.yapp.presentation.model.AttendanceType.Companion.mapToEntity


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

        fun Attendance.mapToEntity(): AttendanceEntity {
            return AttendanceEntity(
                sessionId = this.sessionId,
                type = this.attendanceType.mapToEntity()
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

        fun AttendanceType.mapToEntity(): AttendanceTypeEntity {
            return when (this) {
                is AttendanceType.Absent -> AttendanceTypeEntity.Absent
                is AttendanceType.Normal -> AttendanceTypeEntity.Normal
                is AttendanceType.Late -> AttendanceTypeEntity.Late
                is AttendanceType.Admit -> AttendanceTypeEntity.Admit
            }
        }

        fun getAllTypes() : List<AttendanceType> {
            return listOf(Absent, Normal, Late, Admit)
        }
    }

    object Absent : AttendanceType(point = -20, text = "결석")
    object Normal : AttendanceType(point = 0, text = "출석")
    object Late : AttendanceType(point = -10, text = "지각")
    object Admit : AttendanceType(point = 0, text = "출석 인정")
}