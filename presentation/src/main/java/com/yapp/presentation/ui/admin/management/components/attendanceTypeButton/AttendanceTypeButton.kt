package com.yapp.presentation.ui.admin.management.components.attendanceTypeButton

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .wrapContentWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = AttendanceTheme.colors.grayScale.Gray200),
        elevation = null
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = when (state.iconType) {
                    AttendanceTypeButtonState.IconType.ATTEND -> painterResource(id = R.drawable.icon_attend)
                    AttendanceTypeButtonState.IconType.ADMIT -> painterResource(id = R.drawable.icon_attend)
                    AttendanceTypeButtonState.IconType.TARDY -> painterResource(id = R.drawable.icon_tardy)
                    AttendanceTypeButtonState.IconType.ABSENT -> painterResource(id = R.drawable.icon_absent)
                },
                contentDescription = "drop down",
                tint = AttendanceTheme.colors.grayScale.Gray800
            )

            Spacer(modifier = Modifier.width(4.dp))

            Crossfade(targetState = state.label) {
                Text(
                    text = it,
                    modifier = Modifier
                        .height(20.dp)
                        .align(alignment = Alignment.CenterVertically),
                    style = AttendanceTypography.subtitle2,
                    color = AttendanceTheme.colors.grayScale.Gray800
                )
            }
        }
    }
}