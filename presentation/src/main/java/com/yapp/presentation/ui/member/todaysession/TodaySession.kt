package com.yapp.presentation.ui.member.todaysession

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.layoutId
import com.yapp.common.R
import com.yapp.common.theme.*
import com.yapp.common.yds.YDSAppBar
import com.yapp.presentation.R.*
import com.yapp.presentation.ui.AttendanceScreenRoute
import com.yapp.presentation.ui.member.main.BottomNavigationItem

@Composable
fun TodaySession(
    modifier: Modifier = Modifier,
    navigateToScreen: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            YDSAppBar(
                modifier = Modifier.background(color = Gray_200),
                onClickSettings = {
                    navigateToScreen(AttendanceScreenRoute.MEMBER_SETTING.route)
                }
            )
        },
        modifier = modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(color = Gray_200)
        ) {
            TodaysAttendance()
            SessionDescriptionModal(Modifier.weight(1f))
        }
    }
}

@Composable
private fun TodaysAttendance() {
    Image(
        painter = painterResource(id = R.drawable.illust_member_home_disabled),
        contentDescription = null,
        modifier = Modifier
            .layoutId("yappu", "yappu")
            .fillMaxWidth()
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(24.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(20.dp)
            .layoutId("attendanceCard", "attendanceCard"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.icon_check),
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp),
            tint = Color.Unspecified
        )
        Text(
            text = stringResource(id = string.today_session_attendance_before_text),
            color = Gray_600,
        )
    }
}

@Composable
private fun SessionDescriptionModal(modifier: Modifier) {
    Column(
        modifier = modifier
            .layoutId("modal", "modal")
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(Color.White)
            .padding(24.dp),
    ) {
        Text(
            text = "02.07",
            style = AttendanceTypography.body1,
            color = Gray_600
        )

        Text(
            text = "YAPP 3번째 데브 캠프\n및 성과 공유회",
            style = AttendanceTypography.h1,
            color = Gray_1000,
            modifier = Modifier.padding(top = 28.dp)
        )

        Text(
            text = "드디어 마지막 성과 공유를 하는 세션입니다! \n" +
                    "지금까지 하나의 팀으로서 열심히 작업한 결과물을 YAPP 전원에게 보여주세요 \uD83C\uDF89",
            style = AttendanceTypography.body1,
            color = Gray_800,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}