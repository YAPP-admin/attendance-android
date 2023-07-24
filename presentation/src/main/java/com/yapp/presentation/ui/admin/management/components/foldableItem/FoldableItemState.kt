package com.yapp.presentation.ui.admin.management.components.foldableItem

import FoldableHeaderItemState
import androidx.compose.runtime.Stable
import com.yapp.presentation.ui.admin.management.components.foldableItem.foldableContentItem.FoldableContentItemState
import com.yapp.presentation.ui.admin.management.components.foldableItem.foldableContentItem.FoldableContentItemWithButtonState
import com.yapp.presentation.ui.admin.management.components.foldableItem.foldableContentItem.FoldableContentItemWithScoreState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList


/**
 * ## Description
 * Foldable Header와 Content를 묶어놓은 State
 *
 * -
 * ## Type
 * **[[Type 1]]**. [PositionItemWithButtonContentState]
 * - Tab의 기준 : Position
 * - Content Type : ButtonType
 *
 * **[[Type 2]]**. [TeamItemWithButtonContentState]
 * - Tab의 기준 : Team
 * - Content Type : ButtonType []
 *
 * **[[Type 3]]**. [PositionItemWithScoreContentState]
 * - Tab의 기준 : Position
 * - Content Type : ScoreType
 *
 * **[[Type 4]]**. [TeamItemWithScoreContentState]
 * - Tab의 기준 : Team
 * - Content Type : ScoreType
 *
 * ## Params
 * @property headerItem FoldableItem의 Header
 * @property contentItems FoldableItem의 Contents
 * @property flatten  Contents가 [FoldableHeaderItemState.isExpanded]에 따라 Ui에 표시되는 형태를 나타낸다.
 *
 */
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

data class TeamItemWithButtonContentState(
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
    override val contentItems: ImmutableList<FoldableContentItemWithScoreState.PositionType>
) : FoldableItemState {

    val position: String
        get() = headerItem.position

    override fun setHeaderItemExpandable(isExpand: Boolean): FoldableItemState {
        return this.copy(headerItem = headerItem.copy(isExpanded = isExpand))
    }

}

data class TeamItemWithScoreContentState(
    override val headerItem: FoldableHeaderItemState.TeamType,
    override val contentItems: ImmutableList<FoldableContentItemWithScoreState.TeamType>
) : FoldableItemState {

    val teamName: String
        get() = headerItem.teamName

    val teamNumber: Int
        get() = headerItem.teamNumber

    override fun setHeaderItemExpandable(isExpand: Boolean): FoldableItemState {
        return this.copy(headerItem = headerItem.copy(isExpanded = isExpand))
    }

}
