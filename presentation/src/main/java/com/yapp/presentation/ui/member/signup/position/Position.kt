package com.yapp.presentation.ui.member.signup.position

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.theme.Gray_1200
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSButtonLarge
import com.yapp.common.yds.YDSOption
import com.yapp.common.yds.YdsButtonState
import com.yapp.domain.model.types.PositionType
import com.yapp.presentation.R
import com.yapp.presentation.ui.member.signup.position.PositionContract.PositionSideEffect
import com.yapp.presentation.ui.member.signup.position.PositionContract.PositionUiEvent
import kotlinx.coroutines.flow.collect

@Composable
fun Position(
    viewModel: PositionViewModel = hiltViewModel(),
    onClickBackButton: () -> Unit,
    navigateToTeamScreen: (String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PositionSideEffect.NavigateToTeamScreen -> {
                    navigateToTeamScreen(effect.name, effect.position.name)
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
            Column {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = stringResource(R.string.member_signup_choose_position),
                    style = AttendanceTypography.h1,
                    color = Gray_1200
                )
                Spacer(modifier = Modifier.height(28.dp))
                YDSOption(
                    types = PositionType.values().map { it.value },
                    onTypeClicked = { select ->
                        viewModel.setEvent(
                            PositionUiEvent.ChoosePosition(
                                PositionType.values().find { it.value == select })
                        )
                    },
                    selectedType = uiState.position?.value
                )
            }
            YDSButtonLarge(
                text = stringResource(R.string.member_signup_position_next),
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .height(60.dp)
                    .align(Alignment.BottomCenter),
                onClick = {
                    if (uiState.position != null) {
                        viewModel.setEvent(PositionUiEvent.ConfirmPosition)

                    }
                },
                state = if (uiState.position != null) YdsButtonState.ENABLED else YdsButtonState.DISABLED
            )
        }
    }
}
