package com.yapp.data.model

import com.yapp.domain.model.SessionEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class SessionModel(
    @SerialName("session_id")
    val sessionId: Int?,
    val title: String?,
    val date: String?,
    val description: String?
) {
    companion object {
        fun SessionModel.mapToEntity(): SessionEntity {
            return SessionEntity(
                sessionId = sessionId ?: -1,
                title = title ?: "",
                date = date ?: "",
                description = description ?: ""
            )
        }
    }
}