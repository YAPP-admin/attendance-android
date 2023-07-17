package com.yapp.presentation.ui.admin.management.components.foldableItem.foldableContentItem

import androidx.compose.runtime.Stable
import com.yapp.presentation.ui.admin.management.components.attendanceTypeButton.AttendanceTypeButtonState
import com.yapp.presentation.ui.admin.management.components.foldableItem.FoldableItem


@Stable
interface FoldableContentItemState : FoldableItem {
    val memberId: Long
    val label: String
    val subLabel: String
    val attendanceTypeButtonState: AttendanceTypeButtonState

    data class TeamType(
        override val attendanceTypeButtonState: AttendanceTypeButtonState,
        override val memberId: Long,
        val memberName: String,
        val position: String
    ) : FoldableContentItemState {

        override val label: String
            get() = memberName

        override val subLabel: String
            get() = position

    }

    data class PositionType(
        override val attendanceTypeButtonState: AttendanceTypeButtonState,
        override val memberId: Long,
        val memberName: String,
        val teamType: String,
        val teamNumber: Int
    ) : FoldableContentItemState {

        override val label: String
            get() = memberName

        override val subLabel: String
            get() = "$teamType $teamNumber"

    }
}


