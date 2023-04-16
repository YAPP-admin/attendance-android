package com.yapp.presentation.ui.member.signup.team

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.*
import com.yapp.presentation.R
import com.yapp.presentation.ui.member.signup.team.TeamContract.*
import kotlinx.coroutines.flow.collect

@Composable
fun Team(
    viewModel: TeamViewModel = hiltViewModel(),
    onClickBackButton: () -> Unit,
    navigateToMainScreen: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            YDSAppBar(
                modifier = Modifier.background(AttendanceTheme.colors.backgroundColors.background),
                onClickBackButton = onClickBackButton
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        when (uiState.loadState) {
            TeamUiState.LoadState.Loading -> YDSProgressBar()
            TeamUiState.LoadState.Error -> YDSEmptyScreen()
            TeamUiState.LoadState.Idle -> {
                val onTeamTypeClicked: (String) -> Unit by remember {
                    mutableStateOf({ teamType ->
                        viewModel.setEvent(
                            TeamUiEvent.ChooseTeam(teamType)
                        )
                    })
                }

                val onTeamNumberClicked: (Int) -> Unit by remember {
                    mutableStateOf({ teamNum ->
                        viewModel.setEvent(
                            TeamUiEvent.ChooseTeamNumber(teamNum)
                        )
                    })
                }

                val onConfirmClicked: () -> Unit by remember {
                    mutableStateOf({ viewModel.setEvent(TeamUiEvent.ConfirmTeam) })
                }

                TeamScreen(
                    uiState = uiState,
                    onTeamTypeClicked = onTeamTypeClicked,
                    onTeamNumberClicked = onTeamNumberClicked,
                    onConfirmClicked = onConfirmClicked,
                )
            }
        }

        LaunchedEffect(key1 = viewModel.effect) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is TeamSideEffect.NavigateToMainScreen -> {
                        navigateToMainScreen()
                    }
                    is TeamSideEffect.ShowToast -> {
                        Toast.makeText(context, effect.msg, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}

@Composable
fun TeamScreen(
    uiState: TeamUiState,
    onTeamTypeClicked: (String) -> Unit,
    onTeamNumberClicked: (Int) -> Unit,
    onConfirmClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AttendanceTheme.colors.backgroundColors.background)
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
                color = AttendanceTheme.colors.grayScale.Gray1200,
            )
            Spacer(modifier = Modifier.height(28.dp))
            YDSOption(
                types = uiState.teams.map { it.type.value },
                selectedType = uiState.teamOptionState.selectedOption?.value,
                onTypeClicked = onTeamTypeClicked
            )
            Spacer(modifier = Modifier.height(52.dp))
            TeamNumberOption(
                uiState = uiState,
                onTeamNumberClicked = onTeamNumberClicked
            )
        }

        YDSButtonLarge(
            text = stringResource(R.string.member_signup_team_confirm),
            modifier = Modifier
                .padding(bottom = 40.dp)
                .height(60.dp)
                .align(Alignment.BottomCenter),
            state = if ((uiState.teamOptionState.selectedOption != null) and (uiState.teamNumberOptionState.selectedOption != null)) YdsButtonState.ENABLED
            else YdsButtonState.DISABLED,
            onClick = {
                if ((uiState.teamOptionState.selectedOption != null) and (uiState.teamNumberOptionState.selectedOption != null)) onConfirmClicked()
            },
        )
    }
}

@Composable
fun TeamNumberOption(uiState: TeamUiState, onTeamNumberClicked: (Int) -> Unit) {
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = uiState.teamOptionState.selectedOption != null,
        enter = slideInVertically { with(density) { -40.dp.roundToPx() } }
                + expandVertically(expandFrom = Alignment.CenterVertically)
                + fadeIn(initialAlpha = 0.3f)
    ) {
        Column {
            Text(
                text = stringResource(R.string.member_signup_choose_team_number),
                style = AttendanceTypography.h3,
                color = AttendanceTheme.colors.grayScale.Gray1200
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                repeat(uiState.numberOfSelectedTeamType ?: 0) { teamNum ->
                    YDSChoiceButton(
                        text = stringResource(R.string.member_signup_team_number, teamNum + 1),
                        modifier = Modifier.padding(end = 12.dp, bottom = 12.dp),
                        state = if (uiState.teamNumberOptionState.selectedOption == teamNum + 1) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                        onClick = { onTeamNumberClicked(teamNum + 1) }
                    )
                }
            }
        }
    }
}
