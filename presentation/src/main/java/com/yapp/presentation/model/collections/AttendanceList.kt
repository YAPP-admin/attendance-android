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
    }

    fun getTodayAttendance(sessionId: Int): Attendance {
        return value[sessionId]
    }

    fun getTotalAttendanceScore(): Int {
        return MAX_SCORE - value.sumOf { it.attendanceType.point }
    }

}