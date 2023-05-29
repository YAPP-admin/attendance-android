package com.yapp.presentation.ui.admin.management.components.tablayout


data class YDSTabLayoutState(
    val tabItems: List<YDSTabLayoutItemState>,
) {

    init {
        require(tabItems.count { it.isSelected } == 1)
    }

    val selectedIndex: Int
        get() = tabItems.indexOfFirst { it.isSelected }

    fun select(targetIndex: Int): YDSTabLayoutState {
        return this.copy(
            tabItems = tabItems.mapIndexed { index, itemState ->
                if (itemState.isSelected) {
                    return@mapIndexed itemState.unSelect()
                }

                itemState
            }.mapIndexed { index, itemState ->
                if (index == targetIndex) {
                    return@mapIndexed itemState.select()
                }

                itemState
            }
        )
    }

}