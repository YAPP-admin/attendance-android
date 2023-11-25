package com.yapp.data.model

import com.yapp.domain.model.Session
import com.yapp.domain.model.types.NeedToAttendType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionEntity(
    @SerialName("session_id")
    val sessionId: Int? = null,
    val title: String? = null,
    val type: String? = null,
    val date: String? = null,
    val description: String? = null,
    val code: String? = null
)

fun SessionEntity.toDomain(): Session {
    return Session(
        sessionId = sessionId!!,
        title = title!!,
        date = date!!,
        description = description!!,
        type = NeedToAttendType.valueOf(type!!),
        code = code ?: ""
    )
}

fun Session.toData(): SessionEntity {
    return SessionEntity(
        sessionId = sessionId,
        title = title,
        date = date,
        description = description,
        type = type.name,
        code = code
    )
}
