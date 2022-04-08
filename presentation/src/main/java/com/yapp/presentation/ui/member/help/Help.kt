package com.yapp.presentation.ui.member.help

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import com.yapp.common.theme.*
import com.yapp.common.yds.YDSAppBar
import com.yapp.presentation.R

@Composable
fun Help( onClickBackButton: () -> Unit) {
    Scaffold(
        topBar = {
            YDSAppBar(
                title = stringResource(id = R.string.help_title),
                onClickBackButton = { onClickBackButton() }
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            BasicInfo()
            DetailInfo()
        }
    }
}

@Composable
private fun BasicInfo() {
    Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 72.dp)) {
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = Yapp_Orange,
                        fontSize = 18.sp
                    )
                ) {
                    append(stringResource(id = R.string.help_basic_info_yapp_text))
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = Gray_1200,
                        fontSize = 18.sp
                    )
                ) {
                    append(stringResource(id = R.string.help_basic_info_title_text))
                }
            }
        )
        Text(
            text = stringResource(id = R.string.help_basic_info_content_text),
            color = Gray_600,
            modifier = Modifier.padding(top = 8.dp),
            fontSize = 16.sp
        )
        ScoreRule(
            type = stringResource(id = R.string.help_basic_info_type_pre_notice_text),
            lateScore = -5,
            absentScore = -15
        )
        ScoreRule(
            type = stringResource(id = R.string.help_basic_info_type_not_notice_text),
            lateScore = -10,
            absentScore = -20
        )
    }
}

@Composable
private fun DetailInfo() {
    Column(
        modifier = Modifier
            .background(Gray_200)
            .fillMaxSize()
            .padding(vertical = 40.dp, horizontal = 24.dp)
    ) {
        Text(
            text = stringResource(id = R.string.help_detail_info_title_text),
            modifier = Modifier.padding(bottom = 12.dp),
            color = Gray_800,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        DetailInfoItem(buildAnnotatedString { append(stringResource(id = R.string.help_detail_info_content1_text)) })
        DetailInfoItem(
            buildAnnotatedString {
                append(stringResource(id = R.string.help_detail_info_content2_start_text))
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = Gray_800
                    )
                ) {
                    append(stringResource(id = R.string.help_detail_info_content2_bold_text))
                }
                append(stringResource(id = R.string.help_detail_info_content2_end_text))
            })
        DetailInfoItem(buildAnnotatedString { append(stringResource(id = R.string.help_detail_info_content3_text)) })
        DetailInfoItem(
            buildAnnotatedString {
                append(stringResource(id = R.string.help_detail_info_content4_start_text))
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = Gray_800
                    )
                ) {
                    append(stringResource(id = R.string.help_detail_info_content4_bold_text))
                }
                append(stringResource(id = R.string.help_detail_info_content4_end_text))
            })
    }
}

@Composable
private fun DetailInfoItem(text: AnnotatedString) {
    Row {
        Text(   // 이미지로 교체 예정
            modifier = Modifier.padding(horizontal = 5.dp),
            text = "·",
            fontSize = 12.sp,
            color = Gray_600
        )
        Text(text = text, fontSize = 12.sp, color = Gray_600)
    }
}

@Composable
private fun ScoreRule(type: String, lateScore: Int, absentScore: Int) {
    Column(modifier = Modifier.padding(top = 28.dp)) {
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = Gray_800,
                        fontSize = 16.sp
                    )
                ) {
                    append(type)
                }
                withStyle(style = SpanStyle(color = Gray_800, fontSize = 16.sp)) {
                    append(stringResource(id = R.string.help_basic_info_type_text))
                }
            }
        )
        ScoreRuleTable(tardyScore = lateScore, absentScore = absentScore)
    }
}

@Composable
fun ScoreRuleTable(tardyScore: Int, absentScore: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(BorderStroke(1.dp, Gray_200)),
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
                .background(Gray_200)
                .padding(12.dp),
            text = topText,
            color = Gray_600,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
        Text(
            text = bottomText,
            color = Gray_800,
            modifier = Modifier
                .fillMaxWidth()
                .padding(19.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
}
