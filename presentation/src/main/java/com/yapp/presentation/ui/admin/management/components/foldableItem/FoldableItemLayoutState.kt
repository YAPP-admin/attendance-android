package com.yapp.presentation.ui.admin.management.components.foldableItem

import androidx.compose.runtime.Stable
import com.yapp.presentation.ui.admin.management.components.foldableContentItem.FoldableItemContentLayoutState
import com.yapp.presentation.ui.admin.management.components.foldableHeaderItem.FoldableItemHeaderLayoutState
import kotlinx.collections.immutable.ImmutableList


@Stable
data class FoldableItemLayoutState(
    val headerState: FoldableItemHeaderLayoutState,
    val contentStates: ImmutableList<FoldableItemContentLayoutState>
)