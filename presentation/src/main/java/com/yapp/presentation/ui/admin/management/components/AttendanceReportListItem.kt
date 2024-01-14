package com.yapp.presentation.ui.admin.management.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTheme.colors
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.util.Spacer
import com.yapp.common.util.runIf
import com.yapp.domain.model.Member
import com.yapp.domain.model.Team
import com.yapp.domain.model.collections.AttendanceList
import com.yapp.domain.model.types.PositionType
import com.yapp.domain.model.types.TeamType
import com.yapp.presentation.R
import com.yapp.presentation.ui.admin.management.components.attendanceTypeButton.AttendanceTypeButtonState

enum class TaskType {
    NOT_YET,
    DONE,
    CANCEL;
}

private data class TaskItemColor(
    val headerText: Color,
    val headerSubText: Color = headerText,
    val headerBackground: Color,
    val content: Color,
)

@Composable
internal fun AttendanceReportTaskItem(
    modifier: Modifier = Modifier,
    member: Member,
    time: String,
    contents: String,
    attendanceType: AttendanceTypeButtonState,
    taskType: TaskType,
    onClickReject: () -> Unit,
    onClickApprove: () -> Unit,
) {
    val taskItemColor = when (taskType) {
        TaskType.NOT_YET -> TaskItemColor(
            headerText = colors.mainColors.YappOrange,
            headerBackground = colors.mainColors.YappOrange.copy(alpha = 0.1f),
            content = colors.grayScale.Gray1200,
        )

        TaskType.DONE -> TaskItemColor(
            headerText = colors.grayScale.Gray1200,
            headerSubText = colors.grayScale.Gray600,
            headerBackground = colors.grayScale.Gray300,
            content = colors.grayScale.Gray1200,
        )

        TaskType.CANCEL -> TaskItemColor(
            headerText = colors.grayScale.Gray600,
            headerBackground = colors.grayScale.Gray300,
            content = colors.grayScale.Gray600,
        )
    }

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = colors.grayScale.Gray300,
                shape = RoundedCornerShape(10.dp),
            )
            .runIf(taskType == TaskType.CANCEL) {
                background(taskItemColor.headerBackground)
            }
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .background(taskItemColor.headerBackground)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${member.name} / ${member.team} / ${member.position.value}",
                style = AttendanceTypography.subtitle2,
                color = taskItemColor.headerText,
            )
            Text(
                text = time,
                style = AttendanceTypography.body2,
                color = taskItemColor.headerSubText,
            )
        }
        Spacer(space = 20.dp)
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = when (attendanceType.iconType) {
                    AttendanceTypeButtonState.IconType.ATTEND -> painterResource(id = R.drawable.icon_attend)
                    AttendanceTypeButtonState.IconType.ADMIT -> painterResource(id = R.drawable.icon_admit)
                    AttendanceTypeButtonState.IconType.TARDY -> painterResource(id = R.drawable.icon_tardy)
                    AttendanceTypeButtonState.IconType.ABSENT -> painterResource(id = R.drawable.icon_absent)
                },
                tint = Color.Unspecified,
                contentDescription = "icon"
            )
            Text(
                text = attendanceType.label,
                style = AttendanceTypography.body2,
                color = taskItemColor.content,
            )
        }
        Spacer(space = 8.dp)
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = contents,
            style = AttendanceTypography.body2,
            color = taskItemColor.content,
        )
        Spacer(space = 20.dp)
        when (taskType) {
            TaskType.NOT_YET -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onClickReject)
                            .weight(1f)
                            .background(colors.grayScale.Gray400),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = stringResource(id = R.string.reject),
                            style = AttendanceTypography.subtitle1,
                            color = Color.White,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onClickApprove)
                            .weight(1f)
                            .background(colors.mainColors.YappOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = stringResource(id = R.string.approve),
                            style = AttendanceTypography.subtitle1,
                            color = Color.White,
                        )
                    }
                }
            }

            TaskType.DONE -> {
                Divider(color = colors.grayScale.Gray300)
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 12.dp),
                        text = stringResource(id = R.string.process_complete),
                        style = AttendanceTypography.subtitle1,
                        color = colors.grayScale.Gray600,
                    )
                }

            }

            TaskType.CANCEL -> {
                Divider(color = Color.White)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(taskItemColor.headerBackground),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 12.dp),
                        text = stringResource(id = R.string.normal_attendance),
                        style = AttendanceTypography.subtitle1,
                        color = colors.grayScale.Gray600,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AttendanceReportTaskItemPreview() {
    AttendanceTheme {
        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(TaskType.values()) {
                AttendanceReportTaskItem(
                    member = Member(
                        0,
                        "상록상록",
                        position = PositionType.DEV_ANDROID,
                        team = Team(TeamType.ANDROID, 2),
                        attendances = AttendanceList.from(
                            emptyList()
                        )
                    ),
                    time = "2:00am",
                    contents = "개인사정으로 지각했는데 봐주세요ㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠ제발유",
                    attendanceType = AttendanceTypeButtonState(
                        "지각",
                        iconType = AttendanceTypeButtonState.IconType.TARDY
                    ),
                    taskType = it,
                    onClickReject = { },
                    onClickApprove = {}
                )
            }
        }
    }
}