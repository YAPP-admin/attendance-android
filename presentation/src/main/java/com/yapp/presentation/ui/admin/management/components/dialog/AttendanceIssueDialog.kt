package com.yapp.presentation.ui.admin.management.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.util.Spacer
import com.yapp.common.yds.YDSButtonMedium
import com.yapp.common.yds.YdsButtonState
import com.yapp.presentation.R
import com.yapp.presentation.ui.admin.management.components.attendanceTypeButton.AttendanceTypeButton
import com.yapp.presentation.ui.admin.management.components.attendanceTypeButton.AttendanceTypeButtonState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun AttendanceIssueDialog(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    attendanceTypes: ImmutableList<AttendanceTypeButtonState>,
    onClickAttendanceType: (Int) -> Unit,
    selectedIndex: Int,
    onDismiss: () -> Unit,
    onClickCompleteButton: () -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White),
            shape = RoundedCornerShape(10.dp),
            color = AttendanceTheme.colors.backgroundColors.backgroundElevated
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(all = 24.dp),
            ) {
                Text(
                    text = title,
                    style = AttendanceTypography.h3,
                    color = AttendanceTheme.colors.grayScale.Gray1200
                )
                Spacer(space = 8.dp)
                Text(
                    text = content,
                    style = AttendanceTypography.subtitle1,
                    color = AttendanceTheme.colors.grayScale.Gray800
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    attendanceTypes.forEachIndexed { index, attendanceType ->
                        AttendanceTypeButton(
                            modifier = Modifier.padding(top = 8.dp),
                            state = attendanceType,
                            selected = index == selectedIndex
                        ) {
                            onClickAttendanceType(index)
                        }
                    }
                }
                Spacer(space = 20.dp)
                YDSButtonMedium(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.complete),
                    state = YdsButtonState.ENABLED,
                    onClick = onClickCompleteButton,
                )
            }
        }
    }
}

@Composable
@Preview
private fun AttendanceIssueDialogPreview() {
    val attendanceTypes = AttendanceTypeButtonState.IconType.values().map {
        AttendanceTypeButtonState(
            label = "출결",
            iconType = it
        )
    }.toList().toImmutableList()
    val selectedIndex = remember { mutableStateOf(0) }
    AttendanceIssueDialog(
        title = "박예령님의 출결이 보고와 다른가요?",
        content = "실제 출결 상태로 수정해주세요.",
        attendanceTypes = attendanceTypes,
        onClickAttendanceType = {
            selectedIndex.value = it
        },
        selectedIndex = selectedIndex.value,
        onDismiss = {},
        onClickCompleteButton = {}
    )
}