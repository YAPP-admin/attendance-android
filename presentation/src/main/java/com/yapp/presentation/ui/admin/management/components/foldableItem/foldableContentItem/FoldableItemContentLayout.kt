package com.yapp.presentation.ui.admin.management.components.foldableItem.foldableContentItem

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.presentation.ui.admin.management.LocalMemberItemLongPressCallback
import com.yapp.presentation.ui.admin.management.components.attendanceTypeButton.AttendanceTypeButton
import com.yapp.presentation.ui.admin.management.components.attendanceTypeButton.AttendanceTypeButtonState


@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
private fun FoldableItemContentLayoutPreview() {
    var teamState by remember {
        mutableStateOf(
            FoldableContentItemState.TeamType(
                memberId = 0L,
                memberName = "장덕철",
                position = "Android",
                attendanceTypeButtonState = AttendanceTypeButtonState(
                    label = "출석",
                    iconType = AttendanceTypeButtonState.IconType.ATTEND
                )
            )
        )
    }

    val positionState by remember {
        mutableStateOf(
            FoldableContentItemState.PositionType(
                memberId = 0L,
                memberName = "장덕철",
                attendanceTypeButtonState = AttendanceTypeButtonState(
                    label = "결석",
                    iconType = AttendanceTypeButtonState.IconType.ABSENT
                ),
                teamNumber = 1,
                teamType = "Android"
            )
        )
    }

    AttendanceTheme {
        Column(modifier = Modifier) {
            FoldableItemContentLayout(
                state = teamState,
                onDropDownClicked = {}
            )

            FoldableItemContentLayout(
                state = positionState,
                onDropDownClicked = {}
            )
        }
    }
}

@Composable
internal fun FoldableItemContentLayout(
    modifier: Modifier = Modifier,
    state: FoldableContentItemState,
    onDropDownClicked: (memberId: Long) -> Unit
) {
    val longPressCallback = LocalMemberItemLongPressCallback.current

    FoldableItemContentLayout(
        modifier = modifier,
        label = state.label,
        subLabel = state.subLabel,
        attendanceTypeButtonState = state.attendanceTypeButtonState,
        onDropDownClicked = { onDropDownClicked(state.memberId) },
        onLongPressed = { longPressCallback?.invoke(state.memberId) }
    )
}

@Composable
private fun FoldableItemContentLayout(
    modifier: Modifier = Modifier,
    label: String,
    subLabel: String,
    attendanceTypeButtonState: AttendanceTypeButtonState,
    onDropDownClicked: () -> Unit = {},
    onLongPressed: () -> Unit = {}
) {
    var visibilityProgress by remember { mutableStateOf(0F) }
    val progressAnimatable = remember { Animatable(0F) }

    LaunchedEffect(key1 = true) {
        progressAnimatable.animateTo(1F, animationSpec = tween()) {
            visibilityProgress = value
        }
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp)
            .background(AttendanceTheme.colors.backgroundColors.background)
            .graphicsLayer {
                alpha = visibilityProgress
                scaleY = visibilityProgress
            }
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    onLongPressed()
                })
            }
    ) {
        val (ydsDropdownButton, textContents) = createRefs()

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .constrainAs(textContents) {
                    start.linkTo(parent.start, margin = 8.dp)
                    top.linkTo(parent.top, margin = 17.5.dp)
                    bottom.linkTo(parent.bottom, margin = 17.5.dp)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                textAlign = TextAlign.Start,
                style = AttendanceTypography.body1,
                color = AttendanceTheme.colors.grayScale.Gray1000
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = subLabel,
                textAlign = TextAlign.Start,
                style = AttendanceTypography.caption,
                color = AttendanceTheme.colors.grayScale.Gray600
            )
        }

        AttendanceTypeButton(
            modifier = Modifier
                .wrapContentHeight()
                .animateContentSize()
                .constrainAs(ydsDropdownButton) {
                    end.linkTo(parent.end, margin = 8.dp)
                    top.linkTo(parent.top, margin = 13.dp)
                    bottom.linkTo(parent.bottom, margin = 13.dp)
                },
            state = attendanceTypeButtonState,
            onClick = { onDropDownClicked.invoke() }
        )
    }

}

