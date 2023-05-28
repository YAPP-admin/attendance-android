package com.yapp.presentation.ui.admin.management.components.attendanceBottomSheet

import com.yapp.domain.model.Attendance


data class AttendanceBottomSheetItemLayoutState(
    val label: String,
    val iconType: IconType,
) {

    val onClickIcon: Attendance.Status
        get() = when (iconType) {
            IconType.ATTEND -> Attendance.Status.NORMAL
            IconType.TARDY -> Attendance.Status.LATE
            IconType.ADMIT -> Attendance.Status.ADMIT
            IconType.ABSENT -> Attendance.Status.ABSENT
        }

    enum class IconType {
        ATTEND, TARDY, ADMIT, ABSENT
    }

}