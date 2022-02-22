package com.yapp.presentation.ui.member.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.presentation.ui.member.main.QRMainContract.*
import kotlinx.coroutines.flow.collect

@Composable
fun Main(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    viewModel: QRMainViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        LaunchedEffect(key1 = viewModel.effect) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is QRMainUiSideEffect.ShowToast -> {
                        scaffoldState.snackbarHostState.showSnackbar("Button Clicked")
                    }
                }
            }
        }

        Column {
            Text(text = uiState.value.time)

            Button(
                modifier = Modifier.wrapContentHeight(),
                onClick = {
                    viewModel.setEvent(QRMainUiEvent.OnButtonClicked)
                }
            ) {
                Text("Click!")
            }
        }
    }
}
