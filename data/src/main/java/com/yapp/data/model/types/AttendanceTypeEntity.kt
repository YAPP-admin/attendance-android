package com.yapp.data.model.types

import com.yapp.domain.model.Attendance
import kotlinx.serialization.Serializable


@Serializable
data class AttendanceTypeEntity(
    val text: String
)

fun AttendanceTypeEntity.toDomain(): Attendance.Status {
    return when (this.text) {
        Attendance.Status.ABSENT.name -> Attendance.Status.ABSENT
        Attendance.Status.LATE.name -> Attendance.Status.LATE
        Attendance.Status.ADMIT.name -> Attendance.Status.ADMIT
        Attendance.Status.NORMAL.name -> Attendance.Status.NORMAL
        else -> error("알 수 없는 AttendanceType입니다.")
    }
}

fun Attendance.Status.toData(): String {
    return this.name.uppercase()
}
