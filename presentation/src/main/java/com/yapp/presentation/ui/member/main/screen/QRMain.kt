package com.yapp.presentation.ui.member.main.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.presentation.ui.member.main.QRMainViewModel
import com.yapp.presentation.ui.member.main.state.QRMainContract.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun Main(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    scope: CoroutineScope = rememberCoroutineScope(),
    viewModel: QRMainViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            Text(text = uiState.value.time)

            Button(
                modifier = Modifier.wrapContentHeight(),
                onClick = {
                    viewModel.setEvent(QRMainUiEvent.onButtonClicked)
                }
            ) {
                Text("Click!")
            }

            scope.launch {
                viewModel.effect.collect {
                    scaffoldState.snackbarHostState.showSnackbar("TEST")
                }
            }
        }
    }
}
