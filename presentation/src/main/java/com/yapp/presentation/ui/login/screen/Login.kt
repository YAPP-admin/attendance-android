package com.yapp.presentation.ui.login.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.util.KakaoTalkLoginProvider
import com.yapp.presentation.ui.login.LoginViewModel
import com.yapp.presentation.ui.login.state.LoginContract.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun Login(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.wrapContentWidth()
        ) {
            Button(onClick = {
                viewModel.setEvent(LoginUiEvent.OnLoginButtonClicked)
            }) {
                Text("카카오 로그인")
            }

            Button(onClick = {
                viewModel.setEvent(LoginUiEvent.OnSkipButtonClicked)
            }) {
                Text("넘어가기")
            }
        }
    }
}
