package com.yapp.presentation.ui.member.main.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.yapp.presentation.ui.member.main.QRMainViewModel
import com.yapp.presentation.ui.member.main.state.QRMainContract
import com.yapp.presentation.ui.member.main.state.QRMainContract.*
import dagger.Component
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
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
