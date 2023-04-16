package com.yapp.domain.model

data class Attendance(
    val sessionId: Int,
    val status: Status
) {
    companion object {
        const val MAX_SESSION = 20
    }

    // Status 작성 순서는 정렬 순서와 동일
    enum class Status(val point: Int, val text: String) {
        ABSENT(point = -20, text = "결석"),
        LATE(point = -10, text = "지각"),
        ADMIT(point = 0, text = "출석 인정"),
        NORMAL(point = 0, text = "출석")
    }
}
