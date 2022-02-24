package com.yapp.presentation.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.R
import com.yapp.common.theme.*
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSFitButtonContainer
import com.yapp.common.yds.YdsButtonState
import com.yapp.presentation.R.*
import com.yapp.presentation.ui.model.SessionModel

@Composable
fun AdminMain(
    viewModel: AdminMainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
        if (uiState.isLoading) {
            CircularProgressBar()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                YappuUserScoreCard()
                GraySpacing(Modifier.height(12.dp))
                ManagementTitle()
                TodaySession()
                Spacing()
                GraySpacing(
                    Modifier
                        .height(1.dp)
                        .padding(horizontal = 24.dp)
                )
                ManagementSubTitle()
                TestSessionItem(uiState.sessions)
            }
        }
    }
}

@Composable
private fun CircularProgressBar() {
    CircularProgressIndicator(
        modifier = Modifier.size(10.dp),
        color = Yapp_Orange,
        strokeWidth = 10.dp
    )

}

fun LazyListScope.Spacing() {
    item {
        Spacer(modifier = Modifier.padding(top = 28.dp))
    }
}

fun LazyListScope.TestSessionItem(sessions: List<SessionModel>) {
    items(sessions) { session ->
        SessionItem(session)
    }
}

fun LazyListScope.YappuUserScoreCard() {
    item {
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
                    text = stringResource(id = string.admin_main_see_all_score_text),
                    modifier = Modifier.padding(top = 4.dp),
                    style = AttendanceTypography.body1,
                    color = Yapp_Orange
                )
            }
        }
    }
}

fun LazyListScope.ManagementTitle() {
    item {
        Text(
            text = stringResource(id = string.admin_main_attend_management_text),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
            style = AttendanceTypography.h1
        )
    }
}

fun LazyListScope.GraySpacing(modifier: Modifier) {
    item {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .background(Gray_200)
        )
    }
}

fun LazyListScope.TodaySession() {
    item {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
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
                    text = stringResource(id = string.admin_main_confirm_button),
                    state = YdsButtonState.ENABLED,
                    modifier = Modifier.height(33.dp)
                ) { }
            }
        }
    }
}

fun LazyListScope.ManagementSubTitle() {
    item {
        Text(
            text = stringResource(id = string.admin_main_see_all_session_text),
            color = Gray_600,
            style = AttendanceTypography.body2,
            modifier = Modifier.padding(start = 24.dp, top = 28.dp, end = 24.dp, bottom = 4.dp)
        )
    }
}

@Composable
fun SessionItem(session: SessionModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 20.dp, horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Text(
                text = session.date,
                color = if (session.description == null) Gray_400 else Gray_1200,
                modifier = Modifier.width(64.dp)
            )
            Text(
                text = session.title,
                color = if (session.description == null) Gray_400 else Gray_1200,
            )
        }

        if (session.description != null) {
            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_right),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
    }
}
