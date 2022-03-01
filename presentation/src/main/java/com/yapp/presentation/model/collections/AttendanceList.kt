package com.yapp.presentation.model.collections

import com.yapp.domain.model.AttendanceTypeEntity
import com.yapp.presentation.model.AttendanceType
import com.yapp.presentation.model.AttendanceType.Companion.mapTo


class AttendanceList private constructor(
    private val value: List<AttendanceType>
) {
    companion object {
        fun of(attendances: List<AttendanceType>): AttendanceList {
            return AttendanceList(attendances.toList())
        }

        const val MAX_SCORE = 100
    }

    fun getTodayAttendance(sessionId: Int): AttendanceType {
        return value[sessionId]
    }

    fun getTotalAttendanceScore(): Int {
        return MAX_SCORE - value.sumOf { it.point }
    }

}