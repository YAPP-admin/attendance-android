package com.yapp.presentation.ui.admin.management.components.foldableItem

import FoldableHeaderItemState
import androidx.compose.runtime.Stable
import com.yapp.presentation.ui.admin.management.components.foldableItem.foldableContentItem.FoldableContentItemState
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

    fun expand(): FoldableItemState

    fun collapse(): FoldableItemState
}

data class PositionItemState(
    override val headerItem: FoldableHeaderItemState.PositionType,
    override val contentItems: ImmutableList<FoldableContentItemState.PositionType>
) : FoldableItemState {

    val position: String
        get() = headerItem.position

    override fun expand(): PositionItemState {
        return this.copy(
            headerItem = headerItem.copy(isExpanded = true),
            contentItems = contentItems
        )
    }

    override fun collapse(): PositionItemState {
        return this.copy(
            headerItem = headerItem.copy(isExpanded = false)
        )
    }

}

data class TeamItemState(
    override val headerItem: FoldableHeaderItemState.TeamType,
    override val contentItems: ImmutableList<FoldableContentItemState.TeamType>
) : FoldableItemState {

    val teamName: String
        get() = headerItem.teamName

    val teamNumber: Int
        get() = headerItem.teamNumber

    override fun expand(): TeamItemState {
        return this.copy(
            headerItem = headerItem.copy(isExpanded = true),
            contentItems = contentItems
        )
    }

    override fun collapse(): TeamItemState {
        return this.copy(
            headerItem = headerItem.copy(isExpanded = false)
        )
    }

}