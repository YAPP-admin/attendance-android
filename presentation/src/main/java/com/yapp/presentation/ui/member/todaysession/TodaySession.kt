package com.yapp.presentation.ui.member.todaysession

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.yapp.common.R
import com.yapp.common.theme.*
import com.yapp.common.yds.YDSAppBar
import com.yapp.presentation.ui.AttendanceScreenRoute

@Composable
fun TodaySession(
    modifier: Modifier = Modifier,
    navigateToScreen: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            YDSAppBar(
                onClickSettings = { navigateToScreen(AttendanceScreenRoute.MEMBER_SETTING.route) }
            )
        },
        modifier = modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White, Color(0xA6CCD2DA))
                    )
                ),
        ) {
            TodaysAttendance()
            SessionDescriptionModal(Modifier.weight(1f))
        }
    }
}

@Composable
private fun TodaysAttendance() {
    Image(
        painter = painterResource(id = R.drawable.illust_member_home),
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
            .layoutId("attendanceCard", "attendanceCard"),
    ) {
        Text(
            text = "아직 출석 전이예요",
            color = Gray_600,
            modifier = Modifier.padding(20.dp)
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