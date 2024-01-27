package com.yapp.data.model

import com.google.firebase.firestore.PropertyName
import com.yapp.domain.model.Session
import com.yapp.domain.model.types.NeedToAttendType
import com.yapp.domain.util.DateParser
import kotlinx.serialization.Serializable

@Serializable
data class SessionEntity(
    @PropertyName("sessionId")
    val sessionId: Int? = null,
    @PropertyName("title")
    val title: String? = null,
    @PropertyName("type")
    val type: String? = null,
    @PropertyName("startTime")
    val startTime: String? = null,
    @PropertyName("description")
    val description: String? = null,
    @PropertyName("code")
    val code: String? = null
)

fun SessionEntity.toDomain(dateParser: DateParser): Session {
    return Session(
        sessionId = sessionId!!,
        title = title!!,
        type = NeedToAttendType.valueOf(type!!),
        startTime = dateParser.parse(rawDate = startTime!!),
        description = description!!,
        code = code!!
    )
}

fun Session.toData(dateParser: DateParser): SessionEntity {
    return SessionEntity(
        sessionId = sessionId,
        title = title,
        type = type.name,
        startTime = dateParser.format(date = startTime),
        description = description,
        code = code
    )
}