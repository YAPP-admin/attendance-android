package com.yapp.presentation.model.collections

import com.yapp.presentation.model.Attendance


class AttendanceList private constructor(
    private val value: List<Attendance>
) {
    companion object {
        fun of(attendances: List<Attendance>): AttendanceList {
            return AttendanceList(attendances.toList())
        }

        const val MAX_SCORE = 100
        const val DEFAULT_UPCOMING_SESSION_ID = -1
    }

    fun getAttendanceBySessionId(sessionId: Int): Attendance {
        return value[sessionId]
    }

    private fun getTotalAttendanceScore(): Int {
        val score = MAX_SCORE + value.sumOf { it.attendanceType.point }
        return if (score > 0) score else 0
    }

    fun getTotalAttendanceScore(upcomingSessionId: Int): Int {
        if (upcomingSessionId == DEFAULT_UPCOMING_SESSION_ID) return getTotalAttendanceScore()
        var totalScore = MAX_SCORE
        value.forEach { attendance ->
            if (attendance.sessionId == upcomingSessionId) {
                return if (totalScore > 0) totalScore else 0
            }
            totalScore += attendance.attendanceType.point
        }
        return if (totalScore > 0) totalScore else 0
    }
}