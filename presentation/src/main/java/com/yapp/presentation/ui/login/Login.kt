package com.yapp.presentation.ui.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.util.KakaoTalkLoginProvider
import com.yapp.common.yds.YDSFullButtonContainer
import com.yapp.common.yds.YdsButtonState
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
            constraintSet = constraintSet(),
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .layoutId("progressBar"),
                    strokeWidth = 5.dp
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .layoutId("introduce"),
                text = stringResource(id = R.string.login_attendance_introduce_text)
            )

            YDSFullButtonContainer(
                text = "건너뛰기",
                modifier = Modifier
                    .layoutId("skipButton"),
                state = YdsButtonState.ENABLED,
                onClick = {
                    viewModel.setEvent(LoginUiEvent.OnSkipButtonClicked)
                }
            )

            YDSFullButtonContainer(
                text = stringResource(id = R.string.login_kakao_login_text),
                modifier = Modifier
                    .layoutId("kakaoLoginButton"),
                state = YdsButtonState.ENABLED,
                onClick = {
                    viewModel.setEvent(LoginUiEvent.OnLoginButtonClicked)
                }
            )
        }
    }
}

private fun constraintSet(): ConstraintSet {
    return ConstraintSet {
        val progressBar = createRefFor("progressBar")
        val introduce = createRefFor("introduce")
        val skipButton = createRefFor("skipButton")
        val kakaoLoginButton = createRefFor("kakaoLoginButton")

        constrain(progressBar) {
            start.linkTo(parent.start)
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end)
            top.linkTo(parent.top)
        }

        constrain(introduce) {
            start.linkTo(parent.start)
            bottom.linkTo(skipButton.top, 60.dp)
            end.linkTo(parent.end)
        }

        constrain(skipButton) {
            start.linkTo(parent.start)
            bottom.linkTo(kakaoLoginButton.top, 8.dp)
            end.linkTo(parent.end)
        }

        constrain(kakaoLoginButton) {
            start.linkTo(parent.start)
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end)
        }
    }
}