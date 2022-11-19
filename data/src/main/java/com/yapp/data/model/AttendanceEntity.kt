package com.yapp.data.model

import com.google.firebase.firestore.PropertyName
import com.yapp.data.model.types.AttendanceTypeEntity
import com.yapp.data.model.types.toData
import com.yapp.data.model.types.toDomain
import com.yapp.domain.model.Attendance
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceEntity(
    @PropertyName("sessionId")
    val sessionId: Int? = null,
    @PropertyName("type")
    val type: AttendanceTypeEntity? = null,
)

fun AttendanceEntity.toDomain(): Attendance {
    return Attendance(
        sessionId = this.sessionId!!,
        type = this.type!!.toDomain()
    )
}

fun Attendance.toData(): AttendanceEntity {
    return AttendanceEntity(
        sessionId = sessionId,
        type = type.toData()
    )
}