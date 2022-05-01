package com.yapp.presentation.ui.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yapp.common.R.drawable
import com.yapp.common.R.raw
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.YDSButtonLarge
import com.yapp.common.yds.YDSPopupDialog
import com.yapp.common.yds.YDSProgressBar
import com.yapp.common.yds.YdsButtonState
import com.yapp.presentation.R
import com.yapp.presentation.ui.login.LoginContract.LoginUiEvent
import com.yapp.presentation.ui.login.LoginContract.LoginUiSideEffect
import kotlinx.coroutines.flow.collect

@Composable
fun Login(
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToQRMainScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
    navigateToAdminScreen: () -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

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

                    is LoginUiSideEffect.NavigateToSignUpScreen -> {
                        navigateToSignUpScreen()
                    }

                    is LoginUiSideEffect.NavigateToAdminScreen -> {
                        navigateToAdminScreen()
                    }
                }
            }
        }

        ConstraintLayout(
            constraintSet = constraintSet(),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(24.dp),
        ) {
            IntroduceTitle()
            SkipButton()
            KakaoLoginButton()
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            YappuImage(
                onClickImage = { viewModel.setEvent(LoginUiEvent.OnYappuImageClicked) }
            )
            if (uiState.isLoading) {
                YDSProgressBar()
            }
        }
    }

    if (uiState.isDialogVisible) {
        var password by remember { mutableStateOf("") }
        YDSPopupDialog(
            title = "암호를 대라!",
            content = "코드 넘버를 입력해주세요",
            negativeButtonText = stringResource(id = R.string.Cancel),
            positiveButtonText = stringResource(id = R.string.Ok),
            onClickPositiveButton = { viewModel.adminLogin(password) },
            onClickNegativeButton = { },
            editTextInitValue = password,
            editTextChangedListener = { password = it },
            editTextHint = "****",
            onDismiss = { }
        )
    }
}

@Composable
private fun YappuImage(
    onClickImage: () -> Unit,
) {
    val composition: LottieCompositionResult =
        rememberLottieComposition(LottieCompositionSpec.RawRes(raw.login_buong))
    LottieAnimation(
        modifier = Modifier.clickable {
            onClickImage.invoke()
        },
        composition = composition.value
    )
}

@Composable
private fun SkipButton(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    YDSButtonLarge(
        text = "건너뛰기",
        modifier = Modifier
            .layoutId("skipButton"),
        state = YdsButtonState.ENABLED,
        onClick = {
            viewModel.setEvent(LoginUiEvent.OnSkipButtonClicked)
        }
    )
}

@Composable
private fun IntroduceTitle() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .layoutId("introduce"),
        text = stringResource(id = R.string.login_attendance_introduce_text),
        style = AttendanceTypography.h1
    )
}

@Composable
private fun KakaoLoginButton(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    Button(
        modifier = Modifier
            .layoutId("kakaoLoginButton")
            .fillMaxWidth()
            .height(45.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            viewModel.setEvent(LoginUiEvent.OnLoginButtonClicked)
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xffFEE500)
        ),
        elevation = null
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(drawable.icon_kakao),
                tint = Color.Unspecified,
                contentDescription = null,
            )
            Text(
                text = stringResource(id = R.string.login_kakao_login_text),
                modifier = Modifier.padding(start = 6.dp),
                color = Color(0xff191919),
                style = AttendanceTypography.body1
            )
        }
    }
}

private fun constraintSet(): ConstraintSet {
    return ConstraintSet {
        val introduce = createRefFor("introduce")
        val skipButton = createRefFor("skipButton")
        val kakaoLoginButton = createRefFor("kakaoLoginButton")

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
