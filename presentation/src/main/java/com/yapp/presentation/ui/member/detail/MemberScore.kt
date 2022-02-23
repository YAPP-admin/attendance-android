package com.yapp.presentation.ui.member.detail

import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.theme.*
import com.yapp.common.yds.YDSAppBar
import com.yapp.presentation.R
import kotlin.random.Random

@Composable
fun MemberScore(
    viewModel: MemberScoreViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            YDSAppBar(
                title = "출결 점수 확인",
                onClickBackButton = {}
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            SemiCircleProgressBar(79f)
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AttendanceTheme {
        val randomValueState = remember { mutableStateOf(0) }
        Column {
            SemiCircleProgressBar(randomValueState.value.toFloat())
            RandomDegreeButton {
                randomValueState.value = it
            }
        }
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
                .padding(start = 64.dp, top = 24.dp, end = 64.dp),
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
                    "지금 내 점수는",
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
fun RandomDegreeButton(onClick: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedButton(
            onClick = {
                onClick(Random.nextInt(0, 180))
            }) {
            Text(
                text = "RandomValue"
            )
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
            topText = "출석",
            topIconResId = R.drawable.icon_attend,
            bottomText = "10"
        )

        AttendanceCell(
            topText = "지각",
            topIconResId = R.drawable.icon_tardy,
            bottomText = "1"
        )

        AttendanceCell(
            topText = "결석",
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
        modifier = Modifier.weight(1f),
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