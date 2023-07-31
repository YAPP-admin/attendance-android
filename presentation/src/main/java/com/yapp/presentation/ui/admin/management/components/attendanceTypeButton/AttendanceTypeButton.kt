package com.yapp.presentation.ui.admin.management.components.attendanceTypeButton

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.common.R
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography


@Preview(backgroundColor = 0x00000000, showBackground = true)
@Composable
private fun AttendanceTypeButtonPreview() {
    var state by remember {
        mutableStateOf(
            AttendanceTypeButtonState(
                label = "출석",
                iconType = AttendanceTypeButtonState.IconType.ATTEND
            )
        )
    }

    AttendanceTheme {
        Box(modifier = Modifier) {
            AttendanceTypeButton(
                state = state,
                onClick = {}
            )
        }
    }
}

@Composable
internal fun AttendanceTypeButton(
    modifier: Modifier = Modifier,
    state: AttendanceTypeButtonState,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AttendanceTheme.colors.grayScale.Gray200)
            .clickable(onClick = onClick)
            .padding(start = 8.dp, end = 12.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = when (state.iconType) {
                AttendanceTypeButtonState.IconType.ATTEND -> painterResource(id = R.drawable.icon_attend)
                AttendanceTypeButtonState.IconType.ADMIT -> painterResource(id = R.drawable.icon_admit)
                AttendanceTypeButtonState.IconType.TARDY -> painterResource(id = R.drawable.icon_tardy)
                AttendanceTypeButtonState.IconType.ABSENT -> painterResource(id = R.drawable.icon_absent)
            },
            tint = Color.Unspecified,
            contentDescription = "drop down"
        )

        Spacer(modifier = Modifier.width(4.dp))

        Crossfade(targetState = state.label, label = "") {
            Text(
                text = it,
                style = AttendanceTypography.subtitle2,
                color = AttendanceTheme.colors.grayScale.Gray800,
            )
        }
    }
}