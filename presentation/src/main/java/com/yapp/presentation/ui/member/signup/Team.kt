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
import com.yapp.common.theme.Gray_1200
import com.yapp.common.yds.*
import com.yapp.presentation.R
import com.yapp.presentation.model.TeamModel

@Composable
fun Team(
    viewModel: TeamViewModel = hiltViewModel(),
    navigateToMainScreen: () -> Unit
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
                Text(
                    text = stringResource(R.string.member_signup_choose_team),
                    style = AttendanceTypography.h1,
                    color = Gray_1200
                )
                Spacer(modifier = Modifier.height(28.dp))
                TeamOption(
                    uiState,
                    onTeamTypeClicked = { viewModel.setEvent(TeamContract.TeamUiEvent.ChooseTeam(it)) })
                Spacer(modifier = Modifier.height(52.dp))
                if (uiState.selectedTeam.teamType.isNotEmpty()) {
                    TeamNumberOption(
                        uiState,
                        onTeamNumberClicked = {
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
                state = if ((uiState.selectedTeam.teamType.isNotEmpty()) and (uiState.selectedTeam.teamNum != TeamModel.TEAM_NOT_SELECTED)) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                onClick = { if ((uiState.selectedTeam.teamType.isNotEmpty()) and (uiState.selectedTeam.teamNum != TeamModel.TEAM_NOT_SELECTED)) navigateToMainScreen() },
            )
        }
    }
}

@Composable
fun TeamOption(uiState: TeamContract.TeamUiState, onTeamTypeClicked: (String) -> Unit) {
    val rowNum = 2
    Column() {
        repeat(uiState.teams.size / rowNum) { row ->
            Row() {
                repeat(rowNum) { index ->
                    val team = uiState.teams[rowNum * row + index]
                    YDSChoiceButton(
                        text = team.teamType,
                        modifier = Modifier.padding(end = 12.dp),
                        state = if (uiState.selectedTeam.teamType == team.teamType) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                        onClick = { onTeamTypeClicked(team.teamType) }
                    )
                }
            }
        }
    }
}

@Composable
fun TeamNumberOption(uiState: TeamContract.TeamUiState, onTeamNumberClicked: (Int) -> Unit) {
    val selectedTeamType = uiState.teams.filter { it.teamType == uiState.selectedTeam.teamType }
    Column(
    ) {
        Text(
            text = stringResource(R.string.member_signup_choose_team_number),
            style = AttendanceTypography.h3,
            color = Gray_1200
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row() {
            repeat(selectedTeamType[0].teamNum) { teamNum ->
                YDSChoiceButton(
                    text = stringResource(R.string.member_signup_team_number, teamNum + 1),
                    modifier = Modifier.padding(end = 12.dp),
                    state = if (uiState.selectedTeam.teamNum == teamNum + 1) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                    onClick = { onTeamNumberClicked(teamNum + 1) }
                )
            }
        }
    }
}