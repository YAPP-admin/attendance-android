package com.yapp.presentation.ui.admin.management.components.foldableItem.foldableContentItem

import FoldableHeaderItemState
import androidx.compose.runtime.Stable
import com.yapp.presentation.ui.admin.management.components.attendanceTypeButton.AttendanceTypeButtonState
import com.yapp.presentation.ui.admin.management.components.foldableItem.FoldableItem

/**
 * FoldableItem은 크게 2종류로 나뉘어 있는데, 그 중 Content를 나타내는 State
 *
 * (Header : [FoldableHeaderItemState] - **Content** [FoldableContentItemState] )
 *
 * @property memberId 멤버의 고유 Id
 * @property label Content Item의 title
 * @property subLabel Content Item의 오른쪽에 표시되는 subtitle
 */
@Stable
interface FoldableContentItemState : FoldableItem {
    val memberId: Long
    val label: String
    val subLabel: String
}

/**
 * [FoldableContentItemState] 의 타입중 하나로
 * trailingContent 로 Button을 가지고 있는 FoldableContent
 */
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

/**
 * [FoldableContentItemState] 의 타입중 하나로
 * trailingContent 로 Score을 가지고 있는 FoldableContent
 */
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


