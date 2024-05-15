package com.yapp.domain.model

import com.yapp.domain.model.types.NeedToAttendType
import java.time.LocalDateTime

data class Session(
    val sessionId: Int,
    val title: String,
    val type: NeedToAttendType,
    val startTime: LocalDateTime,
    val description: String,
    val code: String
) {

    val monthAndDay: String
        get() = String.format(TWO_DIGIT, startTime.month.value) + "." + String.format(TWO_DIGIT, startTime.dayOfMonth)

    companion object {
        private const val TWO_DIGIT = "%02d"
    }

}