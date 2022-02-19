package com.yapp.data.model

import java.sql.Timestamp


internal class Session(
    val sequence: Int?,
    val title: String?,
    val start: Timestamp?,
    val end: Timestamp?,
    val content: String?
) {
}