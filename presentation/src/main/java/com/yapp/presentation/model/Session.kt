package com.yapp.presentation.model

import android.os.Parcelable
import com.yapp.domain.model.SessionEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Session(
    val sessionId: Int,
    val title: String,
    val isOff: Boolean,
    val description: String,
    val date: String
): Parcelable {

    companion object {
        fun SessionEntity.mapTo(): Session {
            return Session(
                sessionId = sessionId,
                title = title,
                isOff = isOff,
                description = description,
                date = date
            )
        }
    }

}