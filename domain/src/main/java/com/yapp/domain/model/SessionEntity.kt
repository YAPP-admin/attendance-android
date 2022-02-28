package com.yapp.domain.model

import java.sql.Timestamp


internal class SessionEntity(
    val session_id: Int?,
    val title: String?,
    val date: String?,
    val description: String?
)