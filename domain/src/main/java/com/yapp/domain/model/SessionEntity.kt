package com.yapp.domain.model


data class SessionEntity(
    val sessionId: Int,
    val title: String,
    val date: String,
    val description: String
)