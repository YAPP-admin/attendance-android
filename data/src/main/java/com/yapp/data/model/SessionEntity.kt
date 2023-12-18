package com.yapp.data.model

import com.google.firebase.firestore.PropertyName
import com.yapp.domain.model.Session
import com.yapp.domain.model.types.NeedToAttendType
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

fun SessionEntity.toDomain(): Session {
    return Session(
        sessionId = sessionId!!,
        title = title!!,
        startTime = startTime!!,
        description = description!!,
        type = NeedToAttendType.valueOf(type!!),
        code = code ?: ""
    )
}

fun Session.toData(): SessionEntity {
    return SessionEntity(
        sessionId = sessionId,
        title = title,
        startTime = startTime,
        description = description,
        type = type.name,
        code = code
    )
}
