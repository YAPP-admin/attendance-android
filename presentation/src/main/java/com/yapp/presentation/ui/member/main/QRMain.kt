package com.yapp.presentation.ui.member.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.yds.*
import com.yapp.presentation.ui.member.main.QRMainContract.*
import kotlinx.coroutines.flow.collect

@Composable
fun Main(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    viewModel: QRMainViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            YDSAppBar(
                onClickBackButton = {}
            )
        },
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        LaunchedEffect(key1 = viewModel.effect) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is QRMainUiSideEffect.ShowToast -> {
                        scaffoldState.snackbarHostState.showSnackbar(effect.msg)
                    }
                }
            }
        }

        if (uiState.value.showDialog) {
            YDSPopupDialog(
                title = "Test",
                content = "테스트입니다",
                negativeButtonText = "취소",
                positiveButtonText = "확인",
                onClickNegativeButton = { viewModel.setEvent(QRMainUiEvent.CloseDialog) },
                onClickPositiveButton = { viewModel.setEvent(QRMainUiEvent.CloseDialog) }
            )
        }

        Column {
            Text(text = uiState.value.time)

            Button(
                modifier = Modifier.wrapContentHeight(),
                onClick = {
                    viewModel.setEvent(QRMainUiEvent.OnSnackBarButtonClicked)
                }
            ) {
                Text("Snack Bar")
            }

            Button(
                modifier = Modifier.wrapContentHeight(),
                onClick = {
                    viewModel.setEvent(QRMainUiEvent.OnDialogButtonClicked)
                }
            ) {
                Text("Dialog")
            }

            YDSDropDownButton(
                text = "미통보 지각",
                onClick = {}
            )

            Row {
                YDSChoiceButtonContainer(
                    text = "1 hihih",
                    state = if (uiState.value.selectedButtonId == 1) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                    onClick = {
                        viewModel.setEvent(QRMainUiEvent.OnClickSelectableButtonClicked(1))
                    }
                )
                YDSChoiceButtonContainer(
                    text = "2 hihih",
                    state = if (uiState.value.selectedButtonId == 2) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                    onClick = {
                        viewModel.setEvent(QRMainUiEvent.OnClickSelectableButtonClicked(2))
                    }
                )
            }
        }
    }
}
