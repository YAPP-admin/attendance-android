package com.yapp.data.model.types

import com.yapp.data.model.types.AttendanceTypeEntity.Companion.TEXT_ABSENT
import com.yapp.data.model.types.AttendanceTypeEntity.Companion.TEXT_ADMIT
import com.yapp.data.model.types.AttendanceTypeEntity.Companion.TEXT_LATE
import com.yapp.data.model.types.AttendanceTypeEntity.Companion.TEXT_NORMAL
import com.yapp.domain.model.Attendance
import kotlinx.serialization.Serializable


@Serializable
data class AttendanceTypeEntity(val text: String = TEXT_ABSENT) {
    companion object {
        const val TEXT_ABSENT = "ABSENT"
        const val TEXT_LATE = "LATE"
        const val TEXT_ADMIT = "ADMIT"
        const val TEXT_NORMAL = "NORMAL"
    }
}

fun AttendanceTypeEntity.toDomain(): Attendance.Status {
    return when (this.text) {
        TEXT_ABSENT -> Attendance.Status.ABSENT
        TEXT_LATE -> Attendance.Status.LATE
        TEXT_ADMIT -> Attendance.Status.ADMIT
        TEXT_NORMAL -> Attendance.Status.NORMAL
        else -> error("알 수 없는 AttendanceType입니다.")
    }
}

fun Attendance.Status.toData(): String {
    return when (this) {
        Attendance.Status.ABSENT -> TEXT_ABSENT
        Attendance.Status.LATE -> TEXT_LATE
        Attendance.Status.ADMIT -> TEXT_ADMIT
        Attendance.Status.NORMAL -> TEXT_NORMAL
        else -> error("알 수 없는 AttendanceType입니다.")
    }
}
