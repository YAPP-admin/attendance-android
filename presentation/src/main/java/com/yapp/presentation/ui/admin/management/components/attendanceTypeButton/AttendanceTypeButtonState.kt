package com.yapp.presentation.ui.admin.management.components.attendanceTypeButton


data class AttendanceTypeButtonState(
    val label: String = "",
    val iconType: IconType = IconType.ATTEND,
) {
    enum class IconType {
        ATTEND, TARDY, ADMIT, ABSENT
    }
}