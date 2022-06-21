package com.yapp.presentation.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QrInformation(
    @SerialName("session_id")
    val sessionId: Int,
    @SerialName("password")
    val password: String,
)