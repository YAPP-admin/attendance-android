package com.yapp.presentation.ui.member.score

import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.theme.*
import com.yapp.common.yds.AttendanceType
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSAttendanceList
import com.yapp.presentation.R
import com.yapp.presentation.model.Session

@Composable
fun MemberScore(
    viewModel: MemberScoreViewModel = hiltViewModel(),
    modifier: Modifier,
    navigateToHelpScreen: () -> Unit,
    navigateToSessionDetail: (Session) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            YDSAppBar(
                title = stringResource(R.string.member_score_title),
            )
        },
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn {
            item {
                HelpIcon(navigateToHelpScreen)
                //todo score 주입 필요!
                SemiCircleProgressBar(60f)
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                )
                UserAttendanceTable()
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .background(color = Gray_200)
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                )
            }
            items(uiState.sessions) { session ->
                AttendUserSession(session, navigateToSessionDetail)
            }
        }
    }
}

@Composable
private fun HelpIcon(navigateToHelpScreen: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
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
fun SemiCircleProgressBar(score: Float) {
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
        color = Gray_1000.toArgb()
        typeface = ResourcesCompat.getFont(LocalContext.current, R.font.pretendard_bold)
    }

    val myScoreDescriptionText = stringResource(id = R.string.member_score_now_my_score_text)
    val myScoreDescriptionPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        textSize = 50f
        color = Gray_600.toArgb()
        typeface = ResourcesCompat.getFont(LocalContext.current, R.font.pretendard_medium)
    }

    Column(
        verticalArrangement = Arrangement.Center,
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .padding(start = 64.dp, end = 64.dp),
        ) {
            androidx.compose.foundation.Canvas(
                modifier = Modifier
                    .size(maxWidth, (maxWidth.value / 2).dp)
            ) {
                drawArc(
                    color = Gray_200,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    size = Size(constraints.maxWidth.toFloat(), constraints.maxWidth.toFloat()),
                    style = Stroke(width = 25f, cap = StrokeCap.Round)
                )

                drawArc(
                    color = fillColorByUserScore(score.toInt()),
                    startAngle = 180f,
                    // 반원은 180도, 180도일 때 100점으로 환산하기 위해 9/5 를 곱한다.
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
                    score.toInt().toString(),
                    center.x,
                    (center.y * 2) - 10f,
                    scoreTextPaint
                )
            }
        }
    }
}

@Composable
fun UserAttendanceTable() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(BorderStroke(1.dp, Gray_200)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        AttendanceCell(
            topText = stringResource(R.string.member_score_attend_text),
            topIconResId = R.drawable.icon_attend,
            bottomText = "10"
        )

        AttendanceCell(
            topText = stringResource(R.string.member_score_tardy_text),
            topIconResId = R.drawable.icon_tardy,
            bottomText = "1"
        )

        AttendanceCell(
            topText = stringResource(R.string.member_score_absent_text),
            topIconResId = R.drawable.icon_absent,
            bottomText = "10"
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
                .background(Gray_200)
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
                color = Gray_600,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        Text(
            text = bottomText,
            color = Gray_800,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AttendUserSession(session: Session, navigateToSessionDetail: (Session) -> Unit) {
    YDSAttendanceList(
        attendanceType = if (session.sessionId == 2) AttendanceType.TBD else AttendanceType.ATTEND,
        date = session.date,
        title = session.title,
        description = session.description,
    ) {
        navigateToSessionDetail(session)
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(horizontal = 24.dp)
            .background(Gray_200)
    )
}


private fun fillColorByUserScore(score: Int): Color {
    return when (score) {
        in 80..100 -> Score.GOOD
        in 70..79 -> Score.NORMAL
        else -> Score.DANGEROUS
    }.color
}

enum class Score(val color: Color) {
    GOOD(Etc_Green), NORMAL(Etc_Yellow), DANGEROUS(Etc_Red)
}
