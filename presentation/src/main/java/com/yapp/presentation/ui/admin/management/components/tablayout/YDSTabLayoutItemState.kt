package com.yapp.presentation.ui.admin.management.components.tablayout


data class YDSTabLayoutItemState(
    val isSelected: Boolean,
    val label: String,
) {

    fun select(): YDSTabLayoutItemState {
        return this.copy(isSelected = true)
    }

    fun unSelect(): YDSTabLayoutItemState {
        return this.copy(isSelected = false)
    }

}