package com.yapp.presentation.ui.member.score.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSAttendanceType
import com.yapp.common.yds.YDSEmptyScreen
import com.yapp.common.yds.YDSProgressBar
import com.yapp.common.yds.icon
import com.yapp.common.yds.text

@Composable
fun SessionDetail(
    viewModel: SessionDetailViewModel = hiltViewModel(),
    onClickBackButton: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            YDSAppBar(
                modifier = Modifier.background(AttendanceTheme.colors.backgroundColors.background),
                title = uiState.appBarTitle,
                onClickBackButton = onClickBackButton
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        backgroundColor = AttendanceTheme.colors.backgroundColors.backgroundBase
    ) { contentPadding ->
        when (uiState.loadState) {
            SessionDetailContract.SessionDetailUiState.LoadState.Loading -> YDSProgressBar()
            SessionDetailContract.SessionDetailUiState.LoadState.Error -> YDSEmptyScreen()
            SessionDetailContract.SessionDetailUiState.LoadState.Idle -> SessionDetailScreen(
                modifier = Modifier.padding(contentPadding),
                state = uiState.screenState
            )
        }
    }
}

@Composable
fun SessionDetailScreen(
    modifier: Modifier = Modifier,
    state: SessionDetailContract.SessionDetailUiState.SessionDetailScreenState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AttendanceTheme.colors.backgroundColors.background)
            .padding(horizontal = 24.dp, vertical = 40.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.shouldShowIcon) {
                Icon(
                    painter = state.attendanceType.icon(),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
            Text(
                text = state.attendanceType.text(),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                color = when (state.attendanceType) {
                    YDSAttendanceType.ATTEND -> AttendanceTheme.colors.etcColors.EtcGreen
                    YDSAttendanceType.ABSENT -> AttendanceTheme.colors.etcColors.EtcRed
                    YDSAttendanceType.TARDY -> AttendanceTheme.colors.etcColors.EtcYellowFont
                    YDSAttendanceType.TBD, YDSAttendanceType.NO_ATTENDANCE, YDSAttendanceType.NO_YAPP -> AttendanceTheme.colors.grayScale.Gray400
                }
            )
            Text(
                text = state.date,
                style = AttendanceTypography.body1,
                color = AttendanceTheme.colors.grayScale.Gray600
            )
        }

        Text(
            text = state.title,
            modifier = Modifier.padding(top = 28.dp),
            style = AttendanceTypography.h1,
            color = AttendanceTheme.colors.grayScale.Gray1000
        )

        Text(
            text = state.description,
            modifier = Modifier.padding(top = 12.dp),
            style = AttendanceTypography.body1,
            color = AttendanceTheme.colors.grayScale.Gray800
        )
    }
}

