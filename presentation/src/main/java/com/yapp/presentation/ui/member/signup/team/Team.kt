package com.yapp.presentation.ui.member.signup.team

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.theme.Gray_1200
import com.yapp.common.yds.*
import com.yapp.presentation.R
import kotlinx.coroutines.flow.collect

@Composable
fun Team(
    viewModel: TeamViewModel = hiltViewModel(),
    onClickBackButton: () -> Unit,
    navigateToMainScreen: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TeamContract.TeamSideEffect.NavigateToMainScreen -> {
                    navigateToMainScreen()
                }
                is TeamContract.TeamSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.msg, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    Scaffold(
        topBar = { YDSAppBar(onClickBackButton = { onClickBackButton() }) },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
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
                TeamNumberOption(
                    uiState,
                    onTeamNumberClicked = {
                        viewModel.setEvent(
                            TeamContract.TeamUiEvent.ChooseTeamNumber(it)
                        )
                    })
            }

            YDSButtonLarge(
                text = stringResource(R.string.member_signup_team_confirm),
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .height(60.dp)
                    .align(Alignment.BottomCenter),
                state = if ((uiState.selectedTeamType != null) and (uiState.selectedTeamNumber != null)) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                onClick = {
                    if ((uiState.selectedTeamType != null) and (uiState.selectedTeamNumber != null)) {
                        viewModel.setEvent(TeamContract.TeamUiEvent.ConfirmTeam)
                    }
                },
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
                        text = team.type.value,
                        modifier = Modifier.padding(end = 12.dp, bottom = 12.dp),
                        state = if (uiState.selectedTeamType == team.type) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                        onClick = { onTeamTypeClicked(team.type.value) }
                    )
                }
            }
        }
    }
}

@Composable
fun TeamNumberOption(uiState: TeamContract.TeamUiState, onTeamNumberClicked: (Int) -> Unit) {
    val selectedTeamType = uiState.teams.filter { it.type == uiState.selectedTeamType }
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = uiState.selectedTeamType != null,
        enter = slideInVertically { with(density) { -40.dp.roundToPx() } }
                + expandVertically(expandFrom = Alignment.CenterVertically)
                + fadeIn(initialAlpha = 0.3f)
    ) {
        Column(
        ) {
            Text(
                text = stringResource(R.string.member_signup_choose_team_number),
                style = AttendanceTypography.h3,
                color = Gray_1200
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row() {
                if (selectedTeamType.isNotEmpty()) {
                    repeat(selectedTeamType[0].number!!) { teamNum ->
                        YDSChoiceButton(
                            text = stringResource(R.string.member_signup_team_number, teamNum + 1),
                            modifier = Modifier.padding(end = 12.dp, bottom = 12.dp),
                            state = if (uiState.selectedTeamNumber == teamNum + 1) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                            onClick = { onTeamNumberClicked(teamNum + 1) }
                        )
                    }
                }
            }
        }
    }

}