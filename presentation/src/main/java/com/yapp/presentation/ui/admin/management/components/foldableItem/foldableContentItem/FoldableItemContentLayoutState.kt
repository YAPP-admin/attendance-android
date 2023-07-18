package com.yapp.presentation.ui.admin.management.components.foldableItem.foldableContentItem

import androidx.compose.runtime.Stable
import com.yapp.presentation.ui.admin.management.components.attendanceTypeButton.AttendanceTypeButtonState
import com.yapp.presentation.ui.admin.management.components.foldableItem.FoldableItem


@Stable
interface FoldableContentItemState : FoldableItem {
    val memberId: Long
    val label: String
    val subLabel: String
}

@Stable
interface FoldableContentItemWithButtonState : FoldableContentItemState {
    val attendanceTypeButtonState: AttendanceTypeButtonState

    data class TeamType(
        override val memberId: Long,
        override val attendanceTypeButtonState: AttendanceTypeButtonState,
        val memberName: String,
        val position: String
    ) : FoldableContentItemWithButtonState {

        override val label: String
            get() = memberName

        override val subLabel: String
            get() = position

    }

    data class PositionType(
        override val memberId: Long,
        override val attendanceTypeButtonState: AttendanceTypeButtonState,
        val memberName: String,
        val teamType: String,
        val teamNumber: Int
    ) : FoldableContentItemWithButtonState {

        override val label: String
            get() = memberName

        override val subLabel: String
            get() = "$teamType $teamNumber"

    }
}

@Stable
interface FoldableContentItemWithScoreState : FoldableContentItemState {
    val score: Int
    val shouldShowWarning: Boolean

    data class TeamType(
        override val memberId: Long,
        override val score: Int,
        override val shouldShowWarning: Boolean,
        val memberName: String,
        val position: String
    ) : FoldableContentItemWithScoreState {

        override val label: String
            get() = memberName

        override val subLabel: String
            get() = position

    }

    data class PositionType(
        override val memberId: Long,
        override val score: Int,
        override val shouldShowWarning: Boolean,
        val memberName: String,
        val teamType: String,
        val teamNumber: Int
    ) : FoldableContentItemWithScoreState {

        override val label: String
            get() = memberName

        override val subLabel: String
            get() = "$teamType $teamNumber"

    }

}


