package com.yapp.common.yds

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yapp.common.R
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography

@Composable
fun YDSAttendanceList(
    attendanceType: YDSAttendanceType,
    date: String,
    title: String,
    description: String,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(AttendanceTheme.colors.backgroundColors.background)
            .clickable {
                onClick?.invoke()
            }
            .padding(24.dp)
    ) {
        Icon(
            painter = attendanceType.icon(),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.alpha(
                if ((attendanceType == YDSAttendanceType.TBD) or (attendanceType == YDSAttendanceType.NO_ATTENDANCE) or (attendanceType == YDSAttendanceType.NO_YAPP)) 0f else 1f
            )
        )
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = attendanceType.text(),
                    style = AttendanceTypography.body2,
                    color = when (attendanceType) {
                        YDSAttendanceType.ATTEND -> AttendanceTheme.colors.etcColors.EtcGreen
                        YDSAttendanceType.ABSENT -> AttendanceTheme.colors.etcColors.EtcRed
                        YDSAttendanceType.TARDY -> AttendanceTheme.colors.etcColors.EtcYellowFont
                        YDSAttendanceType.TBD, YDSAttendanceType.NO_ATTENDANCE, YDSAttendanceType.NO_YAPP -> AttendanceTheme.colors.grayScale.Gray400
                    }
                )

                Text(
                    text = date,
                    style = AttendanceTypography.body2,
                    color = AttendanceTheme.colors.grayScale.Gray400
                )
            }

            Text(
                text = title,
                style = AttendanceTypography.h3,
                color = if ((attendanceType == YDSAttendanceType.TBD) or (attendanceType == YDSAttendanceType.NO_YAPP)) AttendanceTheme.colors.grayScale.Gray600 else AttendanceTheme.colors.grayScale.Gray1200,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = description,
                style = AttendanceTypography.body1,
                color = if ((attendanceType == YDSAttendanceType.TBD) or (attendanceType == YDSAttendanceType.NO_YAPP)) AttendanceTheme.colors.grayScale.Gray600 else AttendanceTheme.colors.grayScale.Gray800,
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun YDSAttendanceType.text(): String {
    return stringResource(
        id = when (this) {
            YDSAttendanceType.ATTEND -> R.string.attend
            YDSAttendanceType.TARDY -> R.string.tardy
            YDSAttendanceType.ABSENT -> R.string.absent
            YDSAttendanceType.TBD -> R.string.tbd
            YDSAttendanceType.NO_ATTENDANCE -> R.string.no_attendance
            YDSAttendanceType.NO_YAPP -> R.string.no_yapp
        }
    )
}

@Composable
fun YDSAttendanceType.icon(): Painter {
    return painterResource(
        id = when (this) {
            YDSAttendanceType.ATTEND -> R.drawable.icon_attend
            YDSAttendanceType.TARDY -> R.drawable.icon_tardy
            YDSAttendanceType.ABSENT -> R.drawable.icon_absent
            YDSAttendanceType.TBD -> R.drawable.icon_absent
            YDSAttendanceType.NO_ATTENDANCE -> R.drawable.icon_absent
            YDSAttendanceType.NO_YAPP -> R.drawable.icon_absent
        }
    )
}

enum class YDSAttendanceType {
    ATTEND, TARDY, ABSENT, TBD, NO_ATTENDANCE, NO_YAPP
}
