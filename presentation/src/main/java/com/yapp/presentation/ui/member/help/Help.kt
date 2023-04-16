package com.yapp.presentation.ui.member.help

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.YDSAppBar
import com.yapp.presentation.R

@Composable
fun Help( onClickBackButton: () -> Unit) {
    Scaffold(
        topBar = {
            YDSAppBar(
                modifier = Modifier.background(AttendanceTheme.colors.backgroundColors.background),
                title = stringResource(id = R.string.help_title),
                onClickBackButton = { onClickBackButton() }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(AttendanceTheme.colors.backgroundColors.backgroundBase)
            .systemBarsPadding(),
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)

        ) {
            BasicInfo()
            DetailInfo()
        }
    }
}

@Composable
private fun BasicInfo() {
    Column(
        modifier = Modifier
            .background(AttendanceTheme.colors.backgroundColors.background)
            .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 72.dp)
    ) {
        Text(
            buildAnnotatedString {
                withStyle(
                    style = AttendanceTypography.h3.copy(color = AttendanceTheme.colors.mainColors.YappOrange).toSpanStyle()
                ) {
                    append(stringResource(id = R.string.help_basic_info_yapp_text))
                }
                withStyle(
                    style = AttendanceTypography.h3.copy(color = AttendanceTheme.colors.grayScale.Gray1200).toSpanStyle()
                ) {
                    append(stringResource(id = R.string.help_basic_info_title_text))
                }
            }
        )
        Text(
            text = stringResource(id = R.string.help_basic_info_content_text),
            color = AttendanceTheme.colors.grayScale.Gray600,
            modifier = Modifier.padding(top = 8.dp),
            fontSize = 16.sp,
            style = AttendanceTypography.body1
        )
        ScoreRuleTable(tardyScore = -10, absentScore = -20)
        Spacer(modifier = Modifier.height(38.dp))
        Text(
            text = stringResource(id = R.string.help_basic_info_tip_text),
            color = AttendanceTheme.colors.grayScale.Gray600,
            style = AttendanceTypography.body1
        )
    }
}

@Composable
private fun DetailInfo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AttendanceTheme.colors.grayScale.Gray200)
            .padding(top = 40.dp, start = 24.dp, end=24.dp)
    ) {
        Text(
            text = stringResource(id = R.string.help_detail_info_title_text),
            modifier = Modifier.padding(bottom = 12.dp),
            color = AttendanceTheme.colors.grayScale.Gray600,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
        )

        DetailInfoItem(buildAnnotatedString {
            append(stringResource(id = R.string.help_detail_info_content1_start_text))
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = AttendanceTheme.colors.grayScale.Gray800,
                )
            ) {
                append(stringResource(id = R.string.help_detail_info_content1_bold_text))
            }
            append(stringResource(id = R.string.help_detail_info_content1_end_text))
        })
        DetailInfoItem(buildAnnotatedString {
            append(stringResource(id = R.string.help_detail_info_content2_start_text))
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = AttendanceTheme.colors.grayScale.Gray800
                )
            ) {
                append(stringResource(id = R.string.help_detail_info_content2_bold_text))
            }
            append(stringResource(id = R.string.help_detail_info_content2_end_text))
        })
        DetailInfoItem(buildAnnotatedString { append(stringResource(id = R.string.help_detail_info_content3_text)) })
        DetailInfoItem(buildAnnotatedString { append(stringResource(id = R.string.help_detail_info_content4_text)) })
    }
}

@Composable
private fun DetailInfoItem(text: AnnotatedString) {
    Row {
        Text(   // 이미지로 교체 예정
            modifier = Modifier.padding(horizontal = 5.dp),
            text = "·",
            fontSize = 12.sp,
            color = AttendanceTheme.colors.grayScale.Gray600
        )
        Text(
            text = text,
            color = AttendanceTheme.colors.grayScale.Gray600,
            style = AttendanceTypography.caption.copy(lineHeight = 18.sp),
            fontWeight = FontWeight.W400,
        )
    }
}

@Composable
fun ScoreRuleTable(tardyScore: Int, absentScore: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(BorderStroke(1.dp, AttendanceTheme.colors.grayScale.Gray200)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ScoreRuleCell(
            topText = stringResource(R.string.help_basic_info_tardy_text),
            bottomText = stringResource(R.string.help_basic_info_score_text, tardyScore)
        )

        ScoreRuleCell(
            topText = stringResource(R.string.help_basic_info_absent_text),
            bottomText = stringResource(R.string.help_basic_info_score_text, absentScore)
        )
    }
}

@Composable
fun RowScope.ScoreRuleCell(
    topText: String,
    bottomText: String
) {
    Column(
        modifier = Modifier
            .weight(1f),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(AttendanceTheme.colors.grayScale.Gray200)
                .padding(12.dp),
            text = topText,
            color = AttendanceTheme.colors.grayScale.Gray600,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
        Text(
            text = bottomText,
            color = AttendanceTheme.colors.grayScale.Gray800,
            modifier = Modifier
                .fillMaxWidth()
                .padding(19.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
