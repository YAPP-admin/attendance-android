package com.yapp.presentation.ui.member.signup.position

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.theme.Gray_1200
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSButtonLarge
import com.yapp.common.yds.YDSChoiceButton
import com.yapp.common.yds.YdsButtonState
import com.yapp.presentation.R
import com.yapp.presentation.model.type.PositionType

@Composable
fun Position(
    viewModel: PositionViewModel = hiltViewModel(),
    onClickBackButton: () -> Unit,
    onClickNextButton: (String, String) -> Unit
){
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { YDSAppBar(onClickBackButton = { onClickBackButton() }) },
        modifier = Modifier
            .fillMaxSize()
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ){
            Column {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = stringResource(R.string.member_signup_choose_position),
                    style = AttendanceTypography.h1,
                    color = Gray_1200
                )
                Spacer(modifier = Modifier.height(28.dp))
                PositionOption(uiState.position
                ) { viewModel.setEvent(PositionContract.PositionUiEvent.ChoosePosition(it)) }

            }
            YDSButtonLarge(
                text = stringResource(R.string.member_signup_position_confirm),
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .height(60.dp)
                    .align(Alignment.BottomCenter),
                onClick = {
                },
                state = YdsButtonState.DISABLED
            )
        }
    }
}

@Composable
fun PositionOption(userPosition:PositionType?, onPositionTypeClicked: (PositionType) -> Unit) {
    val rowNum = 2
    val positionList = PositionType.values()
    Column {
        repeat(positionList.size / rowNum) { row ->
            Row {
                repeat(rowNum) { index ->
                    val position = positionList[rowNum * row + index]
                    YDSChoiceButton(
                        text = position.displayName,
                        modifier = Modifier.padding(end = 12.dp),
                        state = if (userPosition == position) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                        onClick = { onPositionTypeClicked(position) }
                    )
                }
            }
        }
    }
}