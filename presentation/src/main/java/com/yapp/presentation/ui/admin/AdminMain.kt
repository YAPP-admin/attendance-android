package com.yapp.presentation.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yapp.common.R
import com.yapp.common.theme.*
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSFitButtonContainer
import com.yapp.common.yds.YdsButtonState

@Composable
fun AdminMain() {
    Scaffold(
        topBar = {
            YDSAppBar(
                onClickBackButton = {},
                onClickSettings = {}
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            item {
                YappuUserScoreCard()
                GraySpacing(12.dp)
                // padding 조절 필요
                Spacer(modifier = Modifier.padding(10.dp))
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    ManagementTitle()
                    Spacer(modifier = Modifier.padding(10.dp))
                    TodaySession()
                    Spacer(modifier = Modifier.padding(10.dp))
                    GraySpacing(1.dp)
                    Spacer(modifier = Modifier.padding(10.dp))
                    ManagementSubTitle()
                    SessionItem()
                }
            }
        }
    }
}

@Composable
fun YappuUserScoreCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(24.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 0.dp,
        backgroundColor = Gray_200
    ) {
        Image(
            painter = painterResource(id = R.drawable.illust_manager_home),
            contentScale = ContentScale.FillWidth,
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "20기 누적 출결 점수",
                style = AttendanceTypography.h3,
                color = Gray_1200
            )
            Text(
                text = "누적 점수 확인하기",
                modifier = Modifier.padding(top = 4.dp),
                style = AttendanceTypography.body1,
                color = Yapp_Orange
            )
        }
    }
}

@Composable
fun ManagementTitle() {
    Text(
        text = "출결 관리",
        style = AttendanceTypography.h1
    )
}

@Composable
fun GraySpacing(height: Dp = 12.dp) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(Gray_200)
    )
}

@Composable
fun TodaySession() {
    Text(
        text = "02.07 오늘",
        color = Gray_600,
        style = AttendanceTypography.body2
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "YAPP 오리엔테이션",
            style = AttendanceTypography.h3
        )
        YDSFitButtonContainer(
            text = "확인",
            state = YdsButtonState.ENABLED,
            modifier = Modifier.height(33.dp)
        ) { }
    }
}

@Composable
fun ManagementSubTitle() {
    Text(
        text = "전체 보기",
        color = Gray_600,
        style = AttendanceTypography.body2,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
fun SessionItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Text(
                text = "01.01",
                modifier = Modifier.width(64.dp)
            )
            Text("제목")
        }

        Icon(Icons.Filled.Menu, contentDescription = null)
    }
}
