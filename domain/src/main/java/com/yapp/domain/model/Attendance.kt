package com.yapp.domain.model

import com.yapp.domain.model.types.AttendanceType

data class Attendance(
    val sessionId: Int,
    val type: AttendanceType
) {
    companion object {
        const val MAX_SESSION = 20
    }
}

