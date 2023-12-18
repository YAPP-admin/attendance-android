package com.yapp.presentation.ui.member.score

import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSAttendanceList
import com.yapp.common.yds.YDSEmptyScreen
import com.yapp.common.yds.YDSProgressBar
import com.yapp.domain.model.Attendance
import com.yapp.domain.model.Session
import com.yapp.domain.model.types.NeedToAttendType
import com.yapp.presentation.R
import com.yapp.presentation.util.attendance.checkSessionAttendance
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MemberScore(
    viewModel: MemberScoreViewModel = hiltViewModel(),
    modifier: Modifier,
    navigateToHelpScreen: () -> Unit,
    navigateToSessionDetail: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            YDSAppBar(
                modifier = Modifier.background(AttendanceTheme.colors.backgroundColors.background),
                title = stringResource(R.string.member_score_title),
            )
        },
        modifier = modifier.fillMaxSize(),
        backgroundColor = AttendanceTheme.colors.backgroundColors.backgroundBase
    ) { contentPadding ->
        when (uiState.loadState) {
            MemberScoreContract.MemberScoreUiState.LoadState.Loading -> YDSProgressBar()
            MemberScoreContract.MemberScoreUiState.LoadState.Error -> YDSEmptyScreen()
            MemberScoreContract.MemberScoreUiState.LoadState.Idle -> MemberScoreScreen(
                modifier = Modifier.padding(contentPadding),
                uiState = uiState,
                navigateToHelpScreen = navigateToHelpScreen,
                navigateToSessionDetail = navigateToSessionDetail
            )
        }

    }
}

@Composable
fun MemberScoreScreen(
    modifier: Modifier = Modifier,
    uiState: MemberScoreContract.MemberScoreUiState,
    navigateToHelpScreen: () -> Unit,
    navigateToSessionDetail: (Int) -> Unit
) {
    val currentScore = uiState.lastAttendanceList.fold(100) { total, pair -> total + pair.second.status.point }

    LazyColumn(
        modifier = modifier.background(AttendanceTheme.colors.backgroundColors.background)
    ) {
        item {
            HelpIcon(navigateToHelpScreen)
            SemiCircleProgressBar(if (currentScore > 0) currentScore else 0)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(AttendanceTheme.colors.backgroundColors.background)
            )
            UserAttendanceTable(uiState.lastAttendanceList)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .background(AttendanceTheme.colors.backgroundColors.backgroundBase)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AttendanceTheme.colors.backgroundColors.background)
            )
        }
        items(uiState.attendanceList) { attendanceInfo ->
            AttendUserSession(attendanceInfo, navigateToSessionDetail)
        }
    }
}


@Composable
private fun HelpIcon(navigateToHelpScreen: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(AttendanceTheme.colors.backgroundColors.background)
    ) {
        Icon(
            painter = painterResource(R.drawable.icon_help),
            contentDescription = "help icon",
            tint = Color.Unspecified,
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .align(Alignment.TopEnd)
                .padding(top = 18.dp, end = 14.dp)
                .clip(CircleShape)
                .clickable {
                    navigateToHelpScreen()
                }
                .padding(10.dp),
        )
    }
}

// 반원형 곡선의 프로그래스 바
@Composable
fun SemiCircleProgressBar(score: Int) {
    val animatedValue = remember { Animatable(0f) }

    LaunchedEffect(animatedValue) {
        animatedValue.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        )
    }

    val scoreTextPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        textSize = 150f
        color = AttendanceTheme.colors.grayScale.Gray1000.toArgb()
        typeface = ResourcesCompat.getFont(LocalContext.current, R.font.pretendard_bold)
    }

    val myScoreDescriptionText = stringResource(id = R.string.member_score_now_my_score_text)
    val myScoreDescriptionPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        textSize = 50f
        color = AttendanceTheme.colors.grayScale.Gray600.toArgb()
        typeface = ResourcesCompat.getFont(LocalContext.current, R.font.pretendard_medium)
    }

    Column(
        verticalArrangement = Arrangement.Center,
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .padding(start = 64.dp, end = 64.dp),
        ) {

            val gray200 = AttendanceTheme.colors.grayScale.Gray200
            val etcGreen = AttendanceTheme.colors.etcColors.EtcGreen
            val etcYellow = AttendanceTheme.colors.etcColors.EtcYellow
            val etcRed = AttendanceTheme.colors.etcColors.EtcRed

            androidx.compose.foundation.Canvas(
                modifier = Modifier
                    .size(maxWidth, (maxWidth.value / 2).dp)
            ) {

                val arcColor = fillColorByUserScore(score).let { score ->
                    when(score) {
                        Score.GOOD -> etcGreen
                        Score.NORMAL -> etcYellow
                        Score.DANGEROUS -> etcRed
                    }
                }

                drawArc(
                    color = gray200,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    size = Size(constraints.maxWidth.toFloat(), constraints.maxWidth.toFloat()),
                    style = Stroke(width = 25f, cap = StrokeCap.Round)
                )


                drawArc(
                    color = arcColor,
                    startAngle = 180f,
                    sweepAngle = (score * 9 / 5) * animatedValue.value,
                    useCenter = false,
                    size = Size(constraints.maxWidth.toFloat(), constraints.maxWidth.toFloat()),
                    style = Stroke(width = 25f, cap = StrokeCap.Round)
                )

                drawContext.canvas.nativeCanvas.drawText(
                    myScoreDescriptionText,
                    center.x,
                    center.y,
                    myScoreDescriptionPaint
                )

                drawContext.canvas.nativeCanvas.drawText(
                    score.toString(),
                    center.x,
                    (center.y * 2) - 10f,
                    scoreTextPaint
                )
            }
        }
    }
}

@Composable
fun UserAttendanceTable(lastAttendanceList: List<Pair<Session, Attendance>>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(BorderStroke(1.dp, AttendanceTheme.colors.grayScale.Gray200)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        AttendanceCell(
            topText = stringResource(R.string.member_score_attend_text),
            topIconResId = R.drawable.icon_attend,
            bottomText = lastAttendanceList
                .filter { it.first.type == NeedToAttendType.NEED_ATTENDANCE }
                .count { (it.second.status == Attendance.Status.NORMAL) or (it.second.status == Attendance.Status.ADMIT) }.toString()
        )

        AttendanceCell(
            topText = stringResource(R.string.member_score_tardy_text),
            topIconResId = R.drawable.icon_tardy,
            bottomText = lastAttendanceList
                .filter { it.first.type == NeedToAttendType.NEED_ATTENDANCE }
                .count { it.second.status == Attendance.Status.LATE }.toString()
        )

        AttendanceCell(
            topText = stringResource(R.string.member_score_absent_text),
            topIconResId = R.drawable.icon_absent,
            bottomText = lastAttendanceList
                .filter { it.first.type == NeedToAttendType.NEED_ATTENDANCE }
                .count { it.second.status == Attendance.Status.ABSENT }.toString()
        )
    }
}

@Composable
fun RowScope.AttendanceCell(
    topText: String,
    topIconResId: Int,
    bottomText: String
) {
    Column(
        modifier = Modifier
            .weight(1f),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(AttendanceTheme.colors.grayScale.Gray200)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = topIconResId),
                contentDescription = null,
                tint = Color.Unspecified,
            )
            Text(
                text = topText,
                color = AttendanceTheme.colors.grayScale.Gray600,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        Text(
            text = bottomText,
            color = AttendanceTheme.colors.grayScale.Gray800,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            textAlign = TextAlign.Center,
            style = AttendanceTypography.h3
        )
    }
}

@Composable
private fun AttendUserSession(
    attendanceInfo: Pair<Session, Attendance>,
    navigateToSessionDetail: (Int) -> Unit
) {
    val session = attendanceInfo.first
    val attendance = attendanceInfo.second

    YDSAttendanceList(
        attendanceType = checkSessionAttendance(session, attendance)!!,
        date = SimpleDateFormat(
            "MM.dd",
            Locale.KOREA
        ).format(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).parse(session.startTime)?.time),
        title = session.title,
        description = session.description,
    ) {
        navigateToSessionDetail(session.sessionId)
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(horizontal = 24.dp)
            .background(AttendanceTheme.colors.backgroundColors.backgroundBase)
    )
}

//TODO Type으로 빼고
private fun fillColorByUserScore(score: Int): Score {
    return when (score) {
        in 80..100 -> Score.GOOD
        in 70..79 -> Score.NORMAL
        else -> Score.DANGEROUS
    }
}

enum class Score {
    GOOD, NORMAL, DANGEROUS
}
