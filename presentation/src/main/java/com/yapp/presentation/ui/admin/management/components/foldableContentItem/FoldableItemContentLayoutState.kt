package com.yapp.presentation.ui.admin.management.components.foldableContentItem

import com.yapp.presentation.ui.admin.management.components.attendanceTypeButton.AttendanceTypeButtonState


data class FoldableItemContentLayoutState(
    val id: Long,
    val label: String,
    val subLabel: String,
    val attendanceTypeButtonState: AttendanceTypeButtonState,
)