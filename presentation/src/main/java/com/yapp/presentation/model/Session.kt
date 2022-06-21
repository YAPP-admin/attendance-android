package com.yapp.presentation.model

import android.os.Parcelable
import com.yapp.domain.model.SessionEntity
import com.yapp.domain.model.types.NeedToAttendType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Session(
    val sessionId: Int,
    val title: String,
    val type: NeedToAttendType,
    val description: String,
    val date: String
) : Parcelable {

    companion object {
        fun SessionEntity.mapTo(): Session {
            return Session(
                sessionId = sessionId,
                title = title,
                type = type,
                description = description,
                date = date
            )
        }
    }

    fun toEntity(): SessionEntity {
        return SessionEntity(
            sessionId = this.sessionId,
            title = this.title,
            type = this.type,
            date = this.date,
            description = this.description
        )
    }
}