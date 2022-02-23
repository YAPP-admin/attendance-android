package com.yapp.presentation.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.util.KakaoTalkLoginProvider
import com.yapp.presentation.R
import com.yapp.presentation.ui.login.LoginContract.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@Composable
fun Login(
    kakaoTalkLoginProvider: KakaoTalkLoginProvider,
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToQRMainScreen: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    viewModel.initKakaoLogin(kakaoTalkLoginProvider)

    AttendanceTheme {
        LaunchedEffect(key1 = viewModel.effect) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is LoginUiSideEffect.ShowToast -> {
                        Toast.makeText(context, effect.msg, Toast.LENGTH_SHORT).show()
                    }
                    is LoginUiSideEffect.NavigateToQRMainScreen -> {
                        navigateToQRMainScreen()
                    }
                }
            }
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            val (skipButton, kakaoTalkLoginButton, introduce, progressBar) = createRefs()

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .constrainAs(progressBar) {
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        },
                    strokeWidth = 5.dp
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(introduce) {
                        start.linkTo(parent.start)
                        bottom.linkTo(skipButton.top, 60.dp)
                        end.linkTo(parent.end)
                    },
                text = stringResource(id = R.string.login_attendance_introduce_text)
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(skipButton) {
                        start.linkTo(parent.start)
                        bottom.linkTo(kakaoTalkLoginButton.top, 8.dp)
                        end.linkTo(parent.end)
                    },
                onClick = {
                    viewModel.setEvent(LoginUiEvent.OnSkipButtonClicked)
                }
            ) {
                Text("넘어가기")
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(kakaoTalkLoginButton) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    },
                onClick = {
                    viewModel.setEvent(LoginUiEvent.OnLoginButtonClicked)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.login_kakao_login_text)
                )
            }
        }
    }
}
