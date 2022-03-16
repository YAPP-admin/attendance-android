package com.yapp.presentation.ui.admin.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.theme.Gray_1200
import com.yapp.common.theme.Yapp_Orange
import com.yapp.common.theme.Yapp_OrangeAlpha
import com.yapp.common.yds.YDSAppBar
import com.yapp.presentation.R


@Preview
@Composable
fun Management(
    viewModel: ManagementViewModel = hiltViewModel(),
    onAppBarBackButtonClicked: (() -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            YDSAppBar(
                title = uiState.title,
                onClickBackButton = {
                    onAppBarBackButtonClicked?.invoke()
                }
            )
        },
        modifier = Modifier
            .fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(28.dp))
                AttendCountText(memberCount = 0)
                Spacer(modifier = Modifier.height(28.dp))
            }
            item {

            }
        }
    }
}

@Preview
@Composable
fun AttendCountText(
    modifier: Modifier = Modifier,
    memberCount: Int = 0
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10))
            .fillMaxWidth()
            .height(48.dp)
            .background(color = Yapp_OrangeAlpha)
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            text = "${memberCount}명이 출석했어요",
            textAlign = TextAlign.Center,
            style = AttendanceTypography.body1,
            color = Yapp_Orange
        )
    }
}

@Preview
@Composable
fun ExpandableTeam(
    modifier: Modifier = Modifier,
    teamName: String = "All-Rounder팀"
) {
    Row(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(62.dp)
    ) {
        Text(
            modifier = Modifier
                .width(0.dp)
                .weight(0.9F)
                .fillMaxHeight()
                .padding(vertical = 20.dp),
            text = teamName,
            textAlign = TextAlign.Start,
            style = AttendanceTypography.h3,
            color = Gray_1200
        )

        IconButton(
            modifier = Modifier
                .weight(0.1F)
                .fillMaxHeight()
                .width(0.dp),
            onClick = { }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_chevron_down),
                tint = Color.Unspecified,
                contentDescription = null
            )
        }

    }
}