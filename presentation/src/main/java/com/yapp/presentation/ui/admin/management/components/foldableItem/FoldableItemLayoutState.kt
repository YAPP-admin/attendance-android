package com.yapp.presentation.ui.admin.management.components.foldableItem

import com.yapp.presentation.ui.admin.management.components.foldableContentItem.FoldableItemContentLayoutState
import com.yapp.presentation.ui.admin.management.components.foldableHeaderItem.FoldableItemHeaderLayoutState


data class FoldableItemLayoutState(
    val headerState: FoldableItemHeaderLayoutState,
    val contentStates: List<FoldableItemContentLayoutState>,
)