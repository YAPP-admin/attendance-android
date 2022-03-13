package com.yapp.data.model

import com.yapp.domain.model.SessionEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SessionModel(
    @SerialName("session_id")
    val sessionId: Int? = null,
    val title: String? = null,
    val date: String? = null,
    val description: String? = null
) {
    companion object {
        fun SessionModel.mapToEntity(): SessionEntity {
            return SessionEntity(
                sessionId = sessionId!!,
                title = title!!,
                date = date!!,
                description = description!!
            )
        }
    }
}