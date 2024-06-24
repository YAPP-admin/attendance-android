package com.yapp.data.model

import com.google.firebase.firestore.PropertyName
import com.yapp.domain.model.Session
import com.yapp.domain.model.types.NeedToAttendType
import com.yapp.domain.util.DateParser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionEntity(
    @SerialName("session_id")
    val sessionId: Int,
    val title: String,
    val type: String,
    @SerialName("date")
    val startTime: String,
    val description: String,
)

fun SessionEntity.toDomain(dateParser: DateParser): Session {
    return Session(
        sessionId = sessionId,
        title = title,
        type = NeedToAttendType.valueOf(type),
        startTime = dateParser.parse(rawDate = startTime),
        description = description
    )
}

fun Session.toData(dateParser: DateParser): SessionEntity {
    return SessionEntity(
        sessionId = sessionId,
        title = title,
        type = type.name,
        startTime = dateParser.format(date = startTime),
        description = description
    )
}
