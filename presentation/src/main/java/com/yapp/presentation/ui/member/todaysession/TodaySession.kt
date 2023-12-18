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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.layoutId
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSEmptyScreen
import com.yapp.common.yds.YDSPopupDialog
import com.yapp.common.yds.YDSProgressBar
import com.yapp.domain.model.Attendance
import com.yapp.domain.model.Session
import com.yapp.presentation.R
import com.yapp.presentation.ui.AttendanceScreenRoute
import com.yapp.presentation.ui.member.todaysession.TodaySessionContract.DialogState
import com.yapp.presentation.ui.member.todaysession.TodaySessionContract.TodaySessionUiEvent
import com.yapp.presentation.ui.member.todaysession.TodaySessionContract.TodaySessionUiSideEffect
import com.yapp.presentation.util.intent.intentToPlayStore
import kotlin.system.exitProcess

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TodaySession(
    modifier: Modifier = Modifier,
    viewModel: TodaySessionViewModel = hiltViewModel(),
    navigateToSetting: (String) -> Unit,
) {
    val context = LocalContext.current
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

    when (uiState.dialogState) {
        DialogState.NONE -> Unit

        DialogState.REQUIRE_UPDATE -> {
            YDSPopupDialog(
                title = stringResource(R.string.required_update_title),
                content = stringResource(R.string.required_update_content),
                positiveButtonText = stringResource(R.string.update_confirm),
                onClickPositiveButton = { viewModel.setEvent(TodaySessionUiEvent.OnUpdateButtonClicked) },
                onDismiss = { }
            )
        }

        DialogState.NECESSARY_UPDATE -> {
            YDSPopupDialog(
                title = stringResource(R.string.necessary_update_title),
                content = stringResource(R.string.necessary_update_content),
                negativeButtonText = stringResource(R.string.update_cancel),
                positiveButtonText = stringResource(R.string.update_confirm),
                onClickPositiveButton = { viewModel.setEvent(TodaySessionUiEvent.OnUpdateButtonClicked) },
                onClickNegativeButton = { viewModel.setEvent(TodaySessionUiEvent.OnCancelButtonClicked) },
                onDismiss = { }
            )
        }

        DialogState.FAIL_INIT_LOAD -> {
            YDSPopupDialog(
                title = stringResource(R.string.fail_load_version_title),
                content = stringResource(R.string.fail_load_version_content),
                positiveButtonText = stringResource(R.string.Exit),
                onClickPositiveButton = { viewModel.setEvent(TodaySessionUiEvent.OnExitButtonClicked) },
                onDismiss = { }
            )
        }
    }

    if (uiState.loadState == TodaySessionContract.LoadState.Loading) {
        YDSProgressBar()
    } else if (uiState.loadState == TodaySessionContract.LoadState.Error) {
        YDSEmptyScreen()
    }

    LaunchedEffect(key1 = true) {
        viewModel.setEvent(TodaySessionUiEvent.OnInitializeComposable)
    }

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TodaySessionUiSideEffect.NavigateToPlayStore -> context.intentToPlayStore()
                is TodaySessionUiSideEffect.ExitProcess -> exitProcess(0)
            }
        }
    }
}

@Composable
private fun TodaysAttendance(attendanceType: Attendance.Status) {
    val imageRsc: Int
    val iconRsc: Int
    val textRsc: Int
    val textColor: Color

    if (attendanceType == Attendance.Status.ABSENT) {
        imageRsc = R.drawable.illust_member_home_disabled
        iconRsc = R.drawable.icon_check_disabled
        textRsc = R.string.today_session_attendance_before_text
        textColor = AttendanceTheme.colors.grayScale.Gray600
    } else {
        imageRsc = R.drawable.illust_member_home_enabled
        iconRsc = R.drawable.icon_check_enabled
        textRsc = R.string.today_session_attendance_after_text
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
        val sessionDate = session?.startTime?.substring(5, 10)?.replace("-", ".")
        Text(
            text = sessionDate ?: "",
            style = AttendanceTypography.body1,
            color = AttendanceTheme.colors.grayScale.Gray600
        )

        Text(
            text = session?.title
                ?: stringResource(id = R.string.admin_main_finish_all_sessions_text),
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
