package com.yapp.presentation.util.attendance

import com.yapp.common.yds.YDSAttendanceType
import com.yapp.domain.model.Attendance
import com.yapp.domain.model.Session
import com.yapp.domain.model.types.AttendanceType
import com.yapp.domain.model.types.NeedToAttendType
import com.yapp.domain.util.DateUtil

fun checkSessionAttendance(
    session: Session?,
    attendance: Attendance?
): YDSAttendanceType? {
    if ((session == null) or (attendance == null)) {
        return null
    }
    if (!DateUtil.isPastSession(session!!.date)) {
        return YDSAttendanceType.TBD
    }
    if (session.type == NeedToAttendType.DONT_NEED_ATTENDANCE) {
        return YDSAttendanceType.NO_ATTENDANCE
    }
    if (session.type == NeedToAttendType.DAY_OFF) {
        return YDSAttendanceType.NO_YAPP
    }
    return when (attendance!!.type) {
        AttendanceType.Absent -> YDSAttendanceType.ABSENT
        AttendanceType.Admit -> YDSAttendanceType.ATTEND
        AttendanceType.Late -> YDSAttendanceType.TARDY
        AttendanceType.Normal -> YDSAttendanceType.ATTEND
    }
}