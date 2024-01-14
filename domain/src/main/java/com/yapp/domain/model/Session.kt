package com.yapp.domain.model

import com.yapp.domain.model.types.NeedToAttendType

data class Session(
    val sessionId: Int,
    val title: String,
    val type: NeedToAttendType,
    val startTime: String,
    val description: String,
    val code: String,
)
