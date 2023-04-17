package com.yapp.presentation.ui.member.todaysession

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.layoutId
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.R
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSEmptyScreen
import com.yapp.common.yds.YDSProgressBar
import com.yapp.domain.model.Session
import com.yapp.domain.model.types.AttendanceType
import com.yapp.presentation.R.string
import com.yapp.presentation.ui.AttendanceScreenRoute

@Composable
fun TodaySession(
    modifier: Modifier = Modifier,
    viewModel: TodaySessionViewModel = hiltViewModel(),
    navigateToSetting: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            YDSAppBar(
                modifier = Modifier.background(color = AttendanceTheme.colors.backgroundColors.backgroundBase),
                onClickSettings = {
                    navigateToSetting(AttendanceScreenRoute.MEMBER_SETTING.route)
                }
            )
        },
        modifier = modifier
            .fillMaxSize(),
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(color = AttendanceTheme.colors.backgroundColors.backgroundBase)
                .padding(contentPadding)
        ) {
            TodaysAttendance(uiState.attendanceType)
            SessionDescriptionModal(uiState.todaySession, Modifier.weight(1f))
        }
    }

    if (uiState.loadState == TodaySessionContract.LoadState.Loading) {
        YDSProgressBar()
    } else if (uiState.loadState == TodaySessionContract.LoadState.Error) {
        YDSEmptyScreen()
    }

    LaunchedEffect(key1 = true) {
        viewModel.setEvent(TodaySessionContract.TodaySessionUiEvent.OnInitializeComposable)
    }
}

@Composable
private fun TodaysAttendance(attendanceType: AttendanceType) {
    val imageRsc: Int
    val iconRsc: Int
    val textRsc: Int
    val textColor: Color

    if (attendanceType == AttendanceType.Absent) {
        imageRsc = R.drawable.illust_member_home_disabled
        iconRsc = R.drawable.icon_check_disabled
        textRsc = string.today_session_attendance_before_text
        textColor = AttendanceTheme.colors.grayScale.Gray600
    } else {
        imageRsc = R.drawable.illust_member_home_enabled
        iconRsc = R.drawable.icon_check_enabled
        textRsc = string.today_session_attendance_after_text
        textColor = AttendanceTheme.colors.mainColors.YappOrange
    }

    Image(
        painter = painterResource(id = imageRsc),
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
            .background(AttendanceTheme.colors.backgroundColors.background)
            .padding(20.dp)
            .layoutId("attendanceCard", "attendanceCard"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRsc),
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp),
            tint = Color.Unspecified
        )
        Text(
            text = stringResource(id = textRsc),
            color = textColor,
        )
    }
}

@Composable
private fun SessionDescriptionModal(session: Session?, modifier: Modifier) {
    Column(
        modifier = modifier
            .layoutId("modal", "modal")
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(AttendanceTheme.colors.backgroundColors.background)
            .padding(24.dp),
    ) {
        val sessionDate = session?.date?.substring(5, 10)?.replace("-", ".")
        Text(
            text = sessionDate ?: "",
            style = AttendanceTypography.body1,
            color = AttendanceTheme.colors.grayScale.Gray600
        )

        Text(
            text = session?.title
                ?: stringResource(id = string.admin_main_finish_all_sessions_text),
            style = AttendanceTypography.h1,
            color = AttendanceTheme.colors.grayScale.Gray1000,
            modifier = Modifier.padding(top = 28.dp)
        )

        Text(
            text = session?.description ?: "",
            style = AttendanceTypography.body1,
            color = AttendanceTheme.colors.grayScale.Gray800,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}