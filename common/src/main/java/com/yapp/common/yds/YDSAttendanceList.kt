package com.yapp.common.yds

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yapp.common.R
import com.yapp.common.theme.*

@Composable
fun YDSAttendanceList(
    attendanceType: AttendanceType,
    date: String,
    title: String,
    description: String,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onClick?.invoke()
            }
            .padding(24.dp)
    ) {
        Icon(
            painterResource(id = attendanceType.icon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.alpha(
                if ((attendanceType == AttendanceType.TBD) or (attendanceType == AttendanceType.NO_ATTENDANCE) or (attendanceType == AttendanceType.NO_YAPP)) 0f else 1f)
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
                    text = stringResource(attendanceType.title),
                    style = AttendanceTypography.body2,
                    color = when (attendanceType) {
                        AttendanceType.ATTEND -> Etc_Green
                        AttendanceType.ABSENT -> Etc_Red
                        AttendanceType.TARDY -> Etc_Yellow_Font
                        AttendanceType.TBD, AttendanceType.NO_ATTENDANCE, AttendanceType.NO_YAPP -> Gray_400
                    }
                )

                Text(
                    text = date,
                    style = AttendanceTypography.body2,
                    color = Gray_400
                )
            }

            Text(
                text = title,
                style = AttendanceTypography.h3,
                color = if ((attendanceType == AttendanceType.TBD) or (attendanceType == AttendanceType.NO_YAPP)) Gray_600 else Gray_1200,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = description,
                style = AttendanceTypography.body1,
                color = if ((attendanceType == AttendanceType.TBD) or (attendanceType == AttendanceType.NO_YAPP)) Gray_600 else Gray_800,
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

enum class AttendanceType(@DrawableRes val icon: Int, @StringRes val title: Int) {
    ATTEND(R.drawable.icon_attend, R.string.attend),
    TARDY(R.drawable.icon_tardy, R.string.tardy),
    ABSENT(R.drawable.icon_absent, R.string.absent),
    TBD(R.drawable.icon_absent, R.string.tbd),
    NO_ATTENDANCE(R.drawable.icon_absent, R.string.no_attendance),
    NO_YAPP(R.drawable.icon_absent, R.string.no_yapp)
}