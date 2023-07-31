package com.yapp.presentation.ui.admin.management.components.attendanceBottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.presentation.R


@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
private fun AttendanceBottomSheetItemLayoutPreview() {
    var states by remember {
        mutableStateOf(
            listOf(
                AttendanceBottomSheetItemLayoutState(label = "출석", iconType = AttendanceBottomSheetItemLayoutState.IconType.ATTEND),
                AttendanceBottomSheetItemLayoutState(label = "지각", iconType = AttendanceBottomSheetItemLayoutState.IconType.TARDY),
                AttendanceBottomSheetItemLayoutState(label = "결석", iconType = AttendanceBottomSheetItemLayoutState.IconType.ABSENT),
                AttendanceBottomSheetItemLayoutState(label = "출석 인정", iconType = AttendanceBottomSheetItemLayoutState.IconType.ADMIT)
            )
        )
    }

    AttendanceTheme {
        Column(modifier = Modifier) {
            for (state in states) {
                AttendanceBottomSheetItemLayout(
                    state = state,
                    onClickItem = {

                    }
                )
            }
        }
    }
}

@Composable
fun AttendanceBottomSheetItemLayout(
    modifier: Modifier = Modifier,
    state: AttendanceBottomSheetItemLayoutState,
    onClickItem: (AttendanceBottomSheetItemLayoutState.IconType) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(color = AttendanceTheme.colors.backgroundColors.backgroundElevated)
            .clickable {
                onClickItem.invoke(state.iconType)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = when (state.iconType) {
                AttendanceBottomSheetItemLayoutState.IconType.ATTEND -> painterResource(id = R.drawable.icon_attend)
                AttendanceBottomSheetItemLayoutState.IconType.ADMIT -> painterResource(id = R.drawable.icon_admit)
                AttendanceBottomSheetItemLayoutState.IconType.TARDY -> painterResource(id = R.drawable.icon_tardy)
                AttendanceBottomSheetItemLayoutState.IconType.ABSENT -> painterResource(id = R.drawable.icon_absent)
            },
            tint = Color.Unspecified,
            contentDescription = "attendance_type_icon"
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            modifier = modifier
                .fillMaxHeight()
                .padding(vertical = 14.dp),
            text = state.label,
            style = AttendanceTypography.subtitle1,
            color = AttendanceTheme.colors.grayScale.Gray1200,
            textAlign = TextAlign.Center,
        )
    }
}