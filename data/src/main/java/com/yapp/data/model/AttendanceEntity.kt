package com.yapp.data.model

import com.google.firebase.firestore.PropertyName
import com.yapp.data.model.types.AttendanceTypeEntity
import com.yapp.data.model.types.toData
import com.yapp.domain.model.Attendance
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceEntity(
    @PropertyName("sessionId")
    val sessionId: Int? = null,
    @PropertyName("status")
    val status: String? = null
)

fun AttendanceEntity.toDomain(): Attendance {
    return Attendance(
        sessionId = this.sessionId!!,
        status = when (status) {
            AttendanceTypeEntity.TEXT_ABSENT -> Attendance.Status.ABSENT
            AttendanceTypeEntity.TEXT_LATE -> Attendance.Status.LATE
            AttendanceTypeEntity.TEXT_ADMIT -> Attendance.Status.ADMIT
            AttendanceTypeEntity.TEXT_NORMAL -> Attendance.Status.NORMAL
            else -> Attendance.Status.ABSENT
        }
    )
}

fun Attendance.toData(): AttendanceEntity {
    return AttendanceEntity(
        sessionId = sessionId,
        status = status.toData()
    )
}
