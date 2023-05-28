package com.yapp.presentation.ui.admin.management.components.foldableContentItem

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.presentation.ui.admin.management.components.attendanceTypeButton.AttendanceTypeButton
import com.yapp.presentation.ui.admin.management.components.attendanceTypeButton.AttendanceTypeButtonState


@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
private fun FoldableItemContentLayoutPreview() {
    var state by remember {
        mutableStateOf(
            FoldableItemContentLayoutState(
                id = 0L,
                label = "",
                subLabel = "",
                attendanceTypeButtonState = AttendanceTypeButtonState()
            )
        )
    }

    AttendanceTheme {
        Box(modifier = Modifier) {
            FoldableItemContentLayout(state = state)
        }
    }
}

@Composable
internal fun FoldableItemContentLayout(
    modifier: Modifier = Modifier,
    state: FoldableItemContentLayoutState,
    onDropDownClicked: (memberId: Long) -> Unit = {},
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(59.dp)
    ) {
        val (ydsDropdownButton, nameText) = createRefs()

        Text(
            modifier = Modifier
                .wrapContentWidth()
                .constrainAs(nameText) {
                    start.linkTo(parent.start, margin = 8.dp)
                    top.linkTo(parent.top, margin = 17.5.dp)
                    bottom.linkTo(parent.bottom, margin = 17.5.dp)
                },
            text = state.label,
            textAlign = TextAlign.Start,
            style = AttendanceTypography.body1,
            color = AttendanceTheme.colors.grayScale.Gray800
        )

        AttendanceTypeButton(
            modifier = Modifier
                .wrapContentHeight()
                .animateContentSize()
                .constrainAs(ydsDropdownButton) {
                    end.linkTo(parent.end, margin = 8.dp)
                    top.linkTo(parent.top, margin = 13.dp)
                    bottom.linkTo(parent.bottom, margin = 13.dp)
                },
            state = state.attendanceTypeButtonState,
            onClick = { onDropDownClicked.invoke(state.id) }
        )
    }

}

