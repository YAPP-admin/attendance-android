package com.yapp.presentation.ui.member.signup

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.google.accompanist.flowlayout.FlowRow
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.*
import com.yapp.presentation.model.TeamModel
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
                TeamOption(uiState, viewModel)
                Spacer(modifier = Modifier.height(52.dp))
                if (uiState.myTeam.team.isNotEmpty()) {
                    TeamNumberOption(uiState, viewModel)
                }
            }

            // 클릭 막아두는거 어떻게?
            YDSFullButtonContainer(
                text = "확인",
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    // 가능하다면 content padding
                    .height(60.dp)
                    .align(Alignment.BottomCenter),
                state = if ((uiState.myTeam.team.isNotEmpty()) and (uiState.myTeam.count != -1)) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                onClick = {},
            )
        }
    }
}

@Composable
fun TeamOption(uiState: TeamContract.TeamUiState, viewModel: TeamViewModel) {
    FlowRow() {
        repeat(uiState.teams.size) { t ->
            val team = uiState.teams[t]
            YDSChoiceButtonContainer(
                text = team.team,
                modifier = Modifier.padding(end = 12.dp),
                state = if (uiState.myTeam.team == team.team) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                onClick = { viewModel.setEvent(TeamContract.TeamUiEvent.ChooseTeam(team.team)) }
            )
        }
    }

}

@Composable
fun TeamNumberOption(uiState: TeamContract.TeamUiState, viewModel: TeamViewModel) {
    val team = uiState.teams.filter { it.team == uiState.myTeam.team }
    Column(
    ) {
        Text(text = "하나만 더 알려주세요", style = AttendanceTypography.h3, color = Color.Black)
        Spacer(modifier = Modifier.height(10.dp))
        FlowRow() {
            repeat(team[0].count) { t ->
                YDSChoiceButtonContainer(
                    text = "${t + 1}팀",
                    modifier = Modifier.padding(end = 12.dp),
                    state = if (uiState.myTeam.count == t + 1) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                    onClick = { viewModel.setEvent(TeamContract.TeamUiEvent.ChooseTeamNumber(t + 1)) }
                )
            }
        }
    }
}