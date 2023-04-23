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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSAttendanceType
import com.yapp.common.yds.YDSEmptyScreen
import com.yapp.common.yds.YDSProgressBar
import com.yapp.domain.model.Session
import com.yapp.presentation.util.attendance.checkSessionAttendance
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SessionDetail(
    viewModel: SessionDetailViewModel = hiltViewModel(),
    onClickBackButton: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val session: Session? = uiState.session?.first
    val attendance = checkSessionAttendance(session, uiState.session?.second)

    Scaffold(
        topBar = {
            YDSAppBar(
                modifier = Modifier.background(AttendanceTheme.colors.backgroundColors.background),
                title = session?.title ?: "",
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
                session = session,
                attendance = attendance
            )
        }
    }
}

@Composable
fun SessionDetailScreen(
    modifier: Modifier = Modifier,
    session: Session?,
    attendance: YDSAttendanceType?
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AttendanceTheme.colors.backgroundColors.background)
            .padding(horizontal = 24.dp, vertical = 40.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (attendance != null) {
                if (attendance in listOf(
                        YDSAttendanceType.ABSENT,
                        YDSAttendanceType.ATTEND,
                        YDSAttendanceType.TARDY
                    )
                ) {
                    Icon(
                        painterResource(attendance.icon),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                }
                Text(
                    text = stringResource(id = attendance.title),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    color = when (attendance) {
                        YDSAttendanceType.ATTEND -> AttendanceTheme.colors.etcColors.EtcGreen
                        YDSAttendanceType.ABSENT -> AttendanceTheme.colors.etcColors.EtcRed
                        YDSAttendanceType.TARDY -> AttendanceTheme.colors.etcColors.EtcYellowFont
                        YDSAttendanceType.TBD, YDSAttendanceType.NO_ATTENDANCE, YDSAttendanceType.NO_YAPP -> AttendanceTheme.colors.grayScale.Gray400
                    }
                )
            }
            if (session != null) {
                val sessionDate =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).parse(session.date)?.time
                Text(
                    text = SimpleDateFormat("MM.dd", Locale.KOREA).format(sessionDate),
                    style = AttendanceTypography.body1,
                    color = AttendanceTheme.colors.grayScale.Gray600
                )
            }

        }

        Text(
            text = session?.title ?: "",
            modifier = Modifier.padding(top = 28.dp),
            style = AttendanceTypography.h1,
            color = AttendanceTheme.colors.grayScale.Gray1000
        )

        Text(
            text = session?.description ?: "",
            modifier = Modifier.padding(top = 12.dp),
            style = AttendanceTypography.body1,
            color = AttendanceTheme.colors.grayScale.Gray800
        )
    }
}

