package com.yapp.presentation.ui.model

import com.google.gson.annotations.SerializedName

data class SessionModel(
    @SerializedName("session_id")
    val sessionId: String = "",
    val title: String = "",
    // Description 이 Null 인 경우 휴얍
    // 휴얍이 아닌 경우에는 EmptyString 이라도 보내야 함.
    val description: String? = null,
    val date: String
) {
}