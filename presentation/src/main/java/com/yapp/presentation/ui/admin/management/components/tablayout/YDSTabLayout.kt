package com.yapp.presentation.ui.admin.management.components.tablayout

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.max
import kotlin.math.roundToInt


private const val LAYOUT_ID_TAB_INDICATOR = "TabIndicator"
private const val LAYOUT_ID_UNSELECTED_TAB_LABEL = "UnSelectedTabLabel"
private const val LAYOUT_ID_SELECTED_TAB_LABEL = "SelectedTabLabel"

private const val SPACE_TAB_ITEM = 20

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
private fun YDSTabLayoutPreview() {
    var state by remember {
        mutableStateOf(
            YDSTabLayoutState(
                tabItems = persistentListOf(
                    YDSTabLayoutItemState(
                        isSelected = true,
                        label = "팀별"
                    ),
                    YDSTabLayoutItemState(
                        isSelected = false,
                        label = "직군별"
                    )
                )
            )
        )
    }

    AttendanceTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            YDSTabLayout(
                modifier = Modifier.align(Alignment.Center),
                tabItems = state.tabItems,
                selectedIndex = state.selectedIndex,
                onTabSelected = {
                    state = state.select(it)
                }
            )
        }
    }
}

@Composable
internal fun YDSTabLayout(
    modifier: Modifier = Modifier,
    tabItems: List<YDSTabLayoutItemState>,
    selectedIndex: Int,
    itemSpace: Int = SPACE_TAB_ITEM,
    onTabSelected: (tabItemIndex: Int) -> Unit,
) {
    require(tabItems.size >= 2) { "최소 2개의 Tab Item을 가지고 있어야 합니다." }
    require(selectedIndex >= 0) { "Invalid selected option [${selectedIndex}" }

    var indicatorPosition by remember { mutableStateOf(0f) }
    val indicatorPositionAnim by animateFloatAsState(targetValue = indicatorPosition)

    Layout(
        modifier = modifier,
        content = {
            tabItems.forEachIndexed { index, item ->
                YDSTabLayoutItem(
                    modifier = Modifier
                        .layoutId(
                            if (item.isSelected) {
                                LAYOUT_ID_SELECTED_TAB_LABEL
                            } else {
                                LAYOUT_ID_UNSELECTED_TAB_LABEL
                            }
                        )
                        .clickable(
                            enabled = item.isSelected.not(),
                            onClick = { onTabSelected(index) },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ),
                    isSelected = item.isSelected,
                    label = item.label
                )

                Box(
                    modifier = Modifier
                        .layoutId(LAYOUT_ID_TAB_INDICATOR)
                        .height(4.dp)
                        .background(color = AttendanceTheme.colors.mainColors.YappOrange)
                )
            }
        },
        measurePolicy = { measurables, constraints ->
            var tabLabelMaxHeight = 0
            var indicatorWidth = 0

            val widthList = mutableListOf<Int>()

            val tabLabelPlaceables = measurables.filter { it.layoutId == LAYOUT_ID_SELECTED_TAB_LABEL || it.layoutId == LAYOUT_ID_UNSELECTED_TAB_LABEL }
                .mapIndexed { index, tabLabelMeasurable ->
                    tabLabelMeasurable.measure(constraints).also { placeable ->
                        tabLabelMaxHeight = max(tabLabelMaxHeight, placeable.height)

                        widthList.add(placeable.width)

                        if (tabLabelMeasurable.layoutId == LAYOUT_ID_SELECTED_TAB_LABEL) {
                            indicatorWidth = placeable.width
                        }
                    }
                }

            val indicatorPlaceable = measurables.first { it.layoutId == LAYOUT_ID_TAB_INDICATOR }
                .measure(
                    Constraints.fixed(
                        width = indicatorWidth,
                        height = 4.dp.toPx().roundToInt(),
                    )
                )

            layout(
                width = (widthList.sum()) + (itemSpace.dp.toPx().roundToInt() * (tabItems.size - 1)),
                height = tabLabelMaxHeight + indicatorPlaceable.height,
            ) {
                var indicationLocation = 0
                tabLabelPlaceables.forEachIndexed { index, placable ->
                    placable.placeRelative(
                        x = if (index == 0) {
                            0
                        } else {
                            val positionX = widthList.take(index).sum() + (itemSpace.dp.toPx().roundToInt() * index)
                            if (index == selectedIndex) {
                                indicationLocation = positionX
                            }

                            positionX
                        },
                        y = 0
                    )
                }

                indicatorPosition = indicationLocation.toFloat()
                indicatorPlaceable.placeRelative(
                    x = indicatorPositionAnim.roundToInt(),
                    y = tabLabelMaxHeight
                )
            }
        }
    )
}

@Composable
private fun YDSTabLayoutItem(
    modifier: Modifier,
    isSelected: Boolean,
    label: String,
) {
    val textColor by animateColorAsState(
        targetValue = if (isSelected) {
            AttendanceTheme.colors.grayScale.Gray1200
        } else {
            AttendanceTheme.colors.grayScale.Gray400
        }
    )

    Column(modifier = modifier) {
        Text(
            text = label,
            style = AttendanceTypography.h3,
            color = textColor
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}

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