package com.yapp.presentation.ui.member.signup

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSFullButtonContainer
import com.yapp.common.yds.YdsButtonState
import com.yapp.presentation.ui.login.LoginContract

@Composable
fun Team(
    viewModel: TeamViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { YDSAppBar(onClickBackButton = {}) },
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart),
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "소속 팀을\n알려주세요", style = AttendanceTypography.h1, color = Color.Black)
                Spacer(modifier = Modifier.height(28.dp))
                TeamOption()
                Spacer(modifier = Modifier.height(52.dp))
                TeamNumberOption()
            }
            YDSFullButtonContainer(
                text = "확인",
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .align(Alignment.BottomCenter),
                state = YdsButtonState.DISABLED,
                onClick = {},
            )
        }
    }
}

@Composable
fun TeamOption() {
}

@Composable
fun TeamNumberOption() {
}