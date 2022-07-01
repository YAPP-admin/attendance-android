package com.yapp.presentation.ui.member.score.detail

import androidx.compose.foundation.layout.*
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
import com.yapp.common.theme.*
import com.yapp.common.yds.AttendanceType
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSEmptyScreen
import com.yapp.common.yds.YDSProgressBar
import com.yapp.presentation.model.Attendance
import com.yapp.presentation.model.Session
import com.yapp.presentation.ui.member.score.MemberScoreContract
import com.yapp.presentation.ui.member.score.MemberScoreScreen
import com.yapp.presentation.util.attendance.checkSessionAttendance
import java.text.SimpleDateFormat
import java.util.*

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
                title = session?.title ?: "",
                onClickBackButton = onClickBackButton
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        when (uiState.loadState) {
            SessionDetailContract.SessionDetailUiState.LoadState.Loading -> YDSProgressBar()
            SessionDetailContract.SessionDetailUiState.LoadState.Error -> YDSEmptyScreen()
            SessionDetailContract.SessionDetailUiState.LoadState.Idle -> SessionDetailScreen(
                session = session,
                attendance = attendance
            )
        }
    }
}

@Composable
fun SessionDetailScreen(session: Session?, attendance: AttendanceType?) {

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                        AttendanceType.ABSENT,
                        AttendanceType.ATTEND,
                        AttendanceType.TARDY
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
                        AttendanceType.ATTEND -> Etc_Green
                        AttendanceType.ABSENT -> Etc_Red
                        AttendanceType.TARDY -> Etc_Yellow_Font
                        AttendanceType.TBD, AttendanceType.NO_ATTENDANCE, AttendanceType.NO_YAPP -> Gray_400
                    }
                )
            }
            if (session != null) {
                val sessionDate =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).parse(session.date)?.time
                Text(
                    text = SimpleDateFormat("MM.dd", Locale.KOREA).format(sessionDate),
                    style = AttendanceTypography.body1,
                    color = Gray_600
                )
            }

        }

        Text(
            text = session?.title ?: "",
            modifier = Modifier.padding(top = 28.dp),
            style = AttendanceTypography.h1,
            color = Gray_1000
        )

        Text(
            text = session?.description ?: "",
            modifier = Modifier.padding(top = 12.dp),
            style = AttendanceTypography.body1,
            color = Gray_800
        )
    }
}

