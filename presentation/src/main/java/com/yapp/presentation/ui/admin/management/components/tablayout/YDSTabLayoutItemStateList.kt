package com.yapp.presentation.ui.admin.management.components.tablayout

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList


data class YDSTabLayoutItemStateList(
    val value: ImmutableList<YDSTabLayoutItemState> = persistentListOf(),
) {

    init {
        require(value.count { it.isSelected } == 1)
    }

    val selectedIndex: Int
        get() = value.indexOfFirst { it.isSelected }

    fun select(targetIndex: Int): YDSTabLayoutItemStateList {
        return this.copy(
            value = value.mapIndexed { index, itemState ->
                if (itemState.isSelected) {
                    return@mapIndexed itemState.unSelect()
                }

                itemState
            }.mapIndexed { index, itemState ->
                if (index == targetIndex) {
                    return@mapIndexed itemState.select()
                }

                itemState
            }.toImmutableList()
        )
    }

}