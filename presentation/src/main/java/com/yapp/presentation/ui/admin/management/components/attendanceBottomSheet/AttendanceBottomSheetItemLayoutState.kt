package com.yapp.presentation.ui.admin.management.components.attendanceBottomSheet

import com.yapp.domain.model.Attendance
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList


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

    companion object {
        fun init(): ImmutableList<AttendanceBottomSheetItemLayoutState> {
            return buildList {
                add(AttendanceBottomSheetItemLayoutState(label = Attendance.Status.NORMAL.text, iconType = AttendanceBottomSheetItemLayoutState.IconType.ATTEND))
                add(AttendanceBottomSheetItemLayoutState(label = Attendance.Status.LATE.text, iconType = AttendanceBottomSheetItemLayoutState.IconType.TARDY))
                add(AttendanceBottomSheetItemLayoutState(label = Attendance.Status.ABSENT.text, iconType = AttendanceBottomSheetItemLayoutState.IconType.ABSENT))
                add(AttendanceBottomSheetItemLayoutState(label = Attendance.Status.ADMIT.text, iconType = AttendanceBottomSheetItemLayoutState.IconType.ADMIT))
            }.toImmutableList()
        }
    }

}