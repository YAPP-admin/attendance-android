package com.yapp.data.model

import kotlinx.serialization.SerialName
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