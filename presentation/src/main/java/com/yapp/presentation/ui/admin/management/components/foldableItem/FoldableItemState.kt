package com.yapp.presentation.ui.admin.management.components.foldableItem

import FoldableHeaderItemState
import androidx.compose.runtime.Stable
import com.yapp.presentation.ui.admin.management.components.foldableItem.foldableContentItem.FoldableContentItemState
import com.yapp.presentation.ui.admin.management.components.foldableItem.foldableContentItem.FoldableContentItemWithButtonState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList


@Stable
interface FoldableItemState {

    val headerItem: FoldableHeaderItemState
    val contentItems: ImmutableList<FoldableContentItemState>

    val flatten: ImmutableList<FoldableItem>
        get() {
            return buildList {
                add(headerItem)
                if (headerItem.isExpanded) {
                    addAll(contentItems)
                }
            }.toImmutableList()
        }

    fun setHeaderItemExpandable(isExpand: Boolean): FoldableItemState

}

data class PositionItemWithButtonContentState(
    override val headerItem: FoldableHeaderItemState.PositionType,
    override val contentItems: ImmutableList<FoldableContentItemWithButtonState.PositionType>
) : FoldableItemState {

    val position: String
        get() = headerItem.position

    override fun setHeaderItemExpandable(isExpand: Boolean): FoldableItemState {
        return this.copy(headerItem = headerItem.copy(isExpanded = isExpand))
    }

}

data class TeamItemButtonWithContentState(
    override val headerItem: FoldableHeaderItemState.TeamType,
    override val contentItems: ImmutableList<FoldableContentItemWithButtonState.TeamType>
) : FoldableItemState {

    val teamName: String
        get() = headerItem.teamName

    val teamNumber: Int
        get() = headerItem.teamNumber

    override fun setHeaderItemExpandable(isExpand: Boolean): FoldableItemState {
        return this.copy(headerItem = headerItem.copy(isExpanded = isExpand))
    }

}

data class PositionItemWithScoreContentState(
    override val headerItem: FoldableHeaderItemState.PositionType,
    override val contentItems: ImmutableList<FoldableContentItemWithButtonState.PositionType>
) : FoldableItemState {

    val position: String
        get() = headerItem.position

    override fun setHeaderItemExpandable(isExpand: Boolean): FoldableItemState {
        return this.copy(headerItem = headerItem.copy(isExpanded = isExpand))
    }

}

data class TeamItemScoreWithContentState(
    override val headerItem: FoldableHeaderItemState.TeamType,
    override val contentItems: ImmutableList<FoldableContentItemWithButtonState.TeamType>
) : FoldableItemState {

    val teamName: String
        get() = headerItem.teamName

    val teamNumber: Int
        get() = headerItem.teamNumber

    override fun setHeaderItemExpandable(isExpand: Boolean): FoldableItemState {
        return this.copy(headerItem = headerItem.copy(isExpanded = isExpand))
    }

}
