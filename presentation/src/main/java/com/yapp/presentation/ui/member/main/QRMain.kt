package com.yapp.presentation.ui.member.main

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.presentation.ui.member.main.QRMainContract.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun Main(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    viewModel: QRMainViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
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
                        Toast.makeText(context, effect.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        Column {
            Text(text = uiState.value.time)

            Button(
                modifier = Modifier.wrapContentHeight(),
                onClick = {
                    // viewModel.setEvent(QRMainUiEvent.OnButtonClicked)
                    // todo: 이런 경우에는 어떻게 처리해야 할까?
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Button Clicked")
                    }
                }
            ) {
                Text("Click!")
            }
        }
    }
}
