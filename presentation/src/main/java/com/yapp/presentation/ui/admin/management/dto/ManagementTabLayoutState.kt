package com.yapp.presentation.ui.admin.management.dto

import com.yapp.presentation.ui.admin.management.components.tablayout.YDSTabLayoutItemState
import com.yapp.presentation.ui.admin.management.components.tablayout.YDSTabLayoutItemStateList
import kotlinx.collections.immutable.toImmutableList

data class ManagementTabLayoutState(
    private val itemsList: YDSTabLayoutItemStateList = YDSTabLayoutItemStateList(),
) {
    val items: List<YDSTabLayoutItemState>
        get() = itemsList.value

    val selectedIndex: Int
        get() = itemsList.selectedIndex

    fun select(index: Int): ManagementTabLayoutState {
        return this.copy(itemsList = itemsList.select(index))
    }

    companion object {
        private const val LABEL_TEAM = "팀별"
        private const val LABEL_POSITION = "직군별"

        const val INDEX_TEAM = 0
        const val INDEX_POSITION = 1

        fun init(): ManagementTabLayoutState {
            return ManagementTabLayoutState(
                itemsList = YDSTabLayoutItemStateList(
                    buildList {
                        add(
                            INDEX_TEAM,
                            YDSTabLayoutItemState(
                                isSelected = true,
                                label = LABEL_TEAM
                            )
                        )

                        add(
                            index = INDEX_POSITION,
                            element = YDSTabLayoutItemState(
                                isSelected = false,
                                label = LABEL_POSITION
                            )
                        )
                    }.toImmutableList()
                )
            )
        }
    }
}