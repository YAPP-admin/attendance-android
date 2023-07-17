package com.yapp.domain.model.collections

import com.yapp.domain.model.Attendance


class AttendanceList private constructor(
    private val value: List<Attendance>,
) : List<Attendance> by value {

    companion object {
        fun from(attendances: List<Attendance>): AttendanceList {
            return AttendanceList(attendances.toList())
        }

        const val MAX_SCORE = 100
        const val DEFAULT_UPCOMING_SESSION_ID = -1
    }

    fun getAttendanceBySessionId(sessionId: Int): Attendance {
        return value[sessionId]
    }

    fun getTotalAttendanceScore(lastSessionId: Int): Int {
        if (lastSessionId == DEFAULT_UPCOMING_SESSION_ID) return getTotalAttendanceScore()
        var totalScore = MAX_SCORE
        value.forEach { attendance ->
            totalScore += attendance.status.point

            if (attendance.sessionId == lastSessionId) {
                return if (totalScore > 0) totalScore else 0
            }
        }
        return if (totalScore > 0) totalScore else 0
    }

    private fun getTotalAttendanceScore(): Int {
        val score = MAX_SCORE + value.sumOf { it.status.point }
        return if (score > 0) score else 0
    }

    // 데이터를 변경하는 행위는 오로지 Domain에서만 가능하게끔 interal을 붙여주었다.
    internal fun changeAttendanceType(sessionId: Int, changingAttendance: Attendance.Status): AttendanceList {
        return this.value.map {
            if (it.sessionId == sessionId) {
                return@map it.copy(status = changingAttendance)
            }

            return@map it
        }.let { AttendanceList(it) }
    }

}
