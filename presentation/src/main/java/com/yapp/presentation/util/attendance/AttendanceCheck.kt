package com.yapp.presentation.util.attendance

import com.yapp.common.yds.AttendanceType
import com.yapp.domain.model.types.NeedToAttendType
import com.yapp.domain.util.DateUtil
import com.yapp.presentation.model.Attendance
import com.yapp.presentation.model.Session

fun checkSessionAttendance(
    session: Session?,
    attendance: Attendance?
): AttendanceType? {
    if ((session == null) or (attendance == null)) {
        return null
    }
    if (!DateUtil.isPastSession(session!!.date)) {
        return AttendanceType.TBD
    }
    if (session.type == NeedToAttendType.DONT_NEED_ATTENDANCE) {
        return AttendanceType.NO_ATTENDANCE
    }
    if (session.type == NeedToAttendType.DAY_OFF) {
        return AttendanceType.NO_YAPP
    }
    return when (attendance!!.attendanceType) {
        com.yapp.presentation.model.AttendanceType.Absent -> AttendanceType.ABSENT
        com.yapp.presentation.model.AttendanceType.Admit -> AttendanceType.ATTEND
        com.yapp.presentation.model.AttendanceType.Late -> AttendanceType.TARDY
        com.yapp.presentation.model.AttendanceType.Normal -> AttendanceType.ATTEND
    }
}