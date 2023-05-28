package com.yapp.presentation.ui.admin.management.components.foldableItem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.common.theme.AttendanceTheme
import com.yapp.domain.model.Attendance
import com.yapp.presentation.ui.admin.management.components.attendanceTypeButton.AttendanceTypeButtonState
import com.yapp.presentation.ui.admin.management.components.foldableContentItem.FoldableItemContentLayout
import com.yapp.presentation.ui.admin.management.components.foldableContentItem.FoldableItemContentLayoutState
import com.yapp.presentation.ui.admin.management.components.foldableHeaderItem.FoldableItemHeaderLayout
import com.yapp.presentation.ui.admin.management.components.foldableHeaderItem.FoldableItemHeaderLayoutState


@Preview
@Composable
private fun FoldableItemLayoutPreview() {
    var state by remember {
        mutableStateOf(
            FoldableItemLayoutState(
                headerState = FoldableItemHeaderLayoutState(
                    label = "Android 1",
                    attendMemberCount = 4,
                    allTeamMemberCount = 6
                ),
                contentStates = listOf(
                    FoldableItemContentLayoutState(
                        id = 0L,
                        label = "김철수",
                        subLabel = "ProducDesign",
                        attendanceTypeButtonState = AttendanceTypeButtonState()
                    ),
                    FoldableItemContentLayoutState(
                        id = 0L,
                        label = "",
                        subLabel = "",
                        attendanceTypeButtonState = AttendanceTypeButtonState()
                    )
                )
            )
        )
    }

    AttendanceTheme {
        Box(modifier = Modifier) {
            FoldableItemLayout(
                state = state,
                onDropDownClicked = {

                }
            )
        }
    }
}

@Composable
internal fun FoldableItemLayout(
    modifier: Modifier = Modifier,
    state: FoldableItemLayoutState,
    onDropDownClicked: ((memberId: Long) -> Unit),
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 24.dp, end = 24.dp)
            .background(AttendanceTheme.colors.backgroundColors.background)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        FoldableItemHeaderLayout(
            state = state.headerState,
            expanded = expanded,
            onExpandClicked = { changedState -> expanded = changedState }
        )

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(animationSpec = tween(50)) +
                    expandVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
            exit = fadeOut(animationSpec = tween(50)) +
                    shrinkVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
        ) {
            Column {
                Divider(
                    modifier = Modifier.height(1.dp),
                    color = AttendanceTheme.colors.grayScale.Gray300
                )
                for (contentItem in state.contentStates) {
                    FoldableItemContentLayout(
                        state = contentItem,
                        onDropDownClicked = { clickedMemberId ->
                            onDropDownClicked.invoke(clickedMemberId)
                        }
                    )
                }
                Divider(
                    modifier = Modifier.height(1.dp),
                    color = AttendanceTheme.colors.grayScale.Gray300
                )
            }
        }
    }
}