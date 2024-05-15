package com.yapp.presentation.common

import com.yapp.common.yds.YDSAttendanceType
import com.yapp.domain.model.Attendance
import com.yapp.domain.model.types.NeedToAttendType
import javax.inject.Inject

class AttendanceTypeMapper @Inject constructor() {

    fun map(
        sessionType: NeedToAttendType,
        attendanceStatus: Attendance.Status,
        isPastSession: Boolean
    ): YDSAttendanceType {
        if (isPastSession.not()) {
            return YDSAttendanceType.TBD
        }
        if (sessionType == NeedToAttendType.DONT_NEED_ATTENDANCE) {
            return YDSAttendanceType.NO_ATTENDANCE
        }
        if (sessionType == NeedToAttendType.DAY_OFF) {
            return YDSAttendanceType.NO_YAPP
        }

        return when (attendanceStatus) {
            Attendance.Status.ABSENT -> YDSAttendanceType.ABSENT
            Attendance.Status.ADMIT -> YDSAttendanceType.ATTEND
            Attendance.Status.LATE -> YDSAttendanceType.TARDY
            Attendance.Status.NORMAL -> YDSAttendanceType.ATTEND
        }
    }

}