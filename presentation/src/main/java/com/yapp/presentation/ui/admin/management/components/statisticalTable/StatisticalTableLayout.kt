package com.yapp.presentation.ui.admin.management.components.statisticalTable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.presentation.ui.admin.management.components.AnimatedCounterText


@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
private fun StatisticalTableLayoutPreview() {
    var state by remember {
        mutableStateOf(
            StatisticalTableLayoutState(
                totalCount = 60,
                tardyCount = 2,
                absentCount = 3,
                admitCount = 0
            )
        )
    }

    AttendanceTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(horizontal = 24.dp)
        ) {
            Button(
                modifier = Modifier.align(Alignment.Center),
                onClick = { state = state.copy(absentCount = state.absentCount + 1) }
            ) {
                Text(text = "dd")
            }

            StatisticalTableLayout(state = state)
        }
    }

}

@Composable
internal fun StatisticalTableLayout(
    state: StatisticalTableLayoutState,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = AttendanceTheme.colors.grayScale.Gray200)
            .padding((1.5).dp)
            .clipToBounds()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedCounterText(
                count = state.attendanceCount,
                style = AttendanceTypography.subtitle2.copy(color = AttendanceTheme.colors.etcColors.EtcGreen),
                prefix = {
                    Text(
                        style = AttendanceTypography.body2,
                        text = "${state.totalCount}명 중 ",
                        color = AttendanceTheme.colors.grayScale.Gray1200
                    )
                },
                suffix = {
                    Text(
                        style = AttendanceTypography.subtitle2,
                        text = "명",
                        color = AttendanceTheme.colors.etcColors.EtcGreen
                    )

                    Text(
                        style = AttendanceTypography.body2,
                        text = "이 출석했어요",
                        color = AttendanceTheme.colors.grayScale.Gray1200
                    )
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                .background(color = AttendanceTheme.colors.backgroundColors.background)
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .clipToBounds(),
            horizontalArrangement = Arrangement.spacedBy(space = 10.dp, alignment = Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TableItemText(
                label = "지각",
                count = state.tardyCount
            )

            Divider(
                modifier = Modifier
                    .width((1.5).dp)
                    .height(16.dp),
                color = AttendanceTheme.colors.grayScale.Gray200,
                thickness = (1.5).dp
            )

            TableItemText(
                label = "결석",
                count = state.absentCount
            )

            Divider(
                modifier = Modifier
                    .width((1.5).dp)
                    .height(16.dp),
                color = AttendanceTheme.colors.grayScale.Gray200,
                thickness = (1.5).dp
            )

            TableItemText(
                label = "출석인정",
                count = state.admitCount
            )
        }
    }
}

@Composable
private fun TableItemText(
    label: String,
    count: Int,
) {
    Row(
        modifier = Modifier.width(80.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = AttendanceTypography.body2,
            color = AttendanceTheme.colors.grayScale.Gray600
        )

        Spacer(modifier = Modifier.width(4.dp))

        AnimatedCounterText(
            count = count,
            style = AttendanceTypography.subtitle2.copy(color = AttendanceTheme.colors.grayScale.Gray1200),
            suffix = {
                Text(
                    text = "명",
                    style = AttendanceTypography.subtitle2,
                    color = AttendanceTheme.colors.grayScale.Gray1200
                )
            }
        )
    }
}