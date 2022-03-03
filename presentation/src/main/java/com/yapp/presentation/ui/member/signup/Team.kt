package com.yapp.presentation.ui.member.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.*
import com.yapp.presentation.R

@Composable
fun Team(
    viewModel: TeamViewModel = hiltViewModel(),
    navigateToMainScreen: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val TEAM_NOT_SELECTED = -1

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
                Text(
                    text = stringResource(R.string.member_signup_choose_team),
                    style = AttendanceTypography.h1,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(28.dp))
                TeamOption(
                    uiState,
                    onTeamChoosed = { viewModel.setEvent(TeamContract.TeamUiEvent.ChooseTeam(it)) })
                Spacer(modifier = Modifier.height(52.dp))
                if (uiState.myTeam.team.isNotEmpty()) {
                    TeamNumberOption(
                        uiState,
                        onTeamNumberChoosed = {
                            viewModel.setEvent(
                                TeamContract.TeamUiEvent.ChooseTeamNumber(it)
                            )
                        })
                }
            }

            YDSButtonLarge(
                text = stringResource(R.string.member_signup_team_confirm),
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .height(60.dp)
                    .align(Alignment.BottomCenter),
                state = if ((uiState.myTeam.team.isNotEmpty()) and (uiState.myTeam.count != TEAM_NOT_SELECTED)) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                onClick = { if ((uiState.myTeam.team.isNotEmpty()) and (uiState.myTeam.count != -1)) navigateToMainScreen() },
            )
        }
    }
}

@Composable
fun TeamOption(uiState: TeamContract.TeamUiState, onTeamChoosed: (String) -> Unit) {
    val rowNum = 2
    Column() {
        repeat(uiState.teams.size / rowNum) { row ->
            Row() {
                repeat(rowNum) { index ->
                    val team = uiState.teams[rowNum * row + index]
                    YDSChoiceButton(
                        text = team.team,
                        modifier = Modifier.padding(end = 12.dp),
                        state = if (uiState.myTeam.team == team.team) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                        onClick = { onTeamChoosed(team.team) }
                    )
                }
            }
        }
    }
}

@Composable
fun TeamNumberOption(uiState: TeamContract.TeamUiState, onTeamNumberChoosed: (Int) -> Unit) {
    val team = uiState.teams.filter { it.team == uiState.myTeam.team }
    val rowNum = 2
    Column(
    ) {
        Text(
            text = stringResource(R.string.member_signup_choose_team_number),
            style = AttendanceTypography.h3,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row() {
            repeat(team[0].count) { teamNum ->
                YDSChoiceButton(
                    text = stringResource(R.string.member_signup_team_number, teamNum + 1),
                    modifier = Modifier.padding(end = 12.dp),
                    state = if (uiState.myTeam.count == teamNum + 1) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                    onClick = { onTeamNumberChoosed(teamNum + 1) }
                )
            }
        }
    }
}