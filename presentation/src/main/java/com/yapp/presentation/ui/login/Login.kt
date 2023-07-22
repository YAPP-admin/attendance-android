package com.yapp.presentation.ui.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import com.google.accompanist.insets.imePadding
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.R.drawable
import com.yapp.common.R.raw
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.YDSPopupDialog
import com.yapp.common.yds.YDSProgressBar
import com.yapp.presentation.R
import com.yapp.presentation.ui.login.LoginContract.DialogState
import com.yapp.presentation.ui.login.LoginContract.LoginUiEvent
import com.yapp.presentation.ui.login.LoginContract.LoginUiSideEffect
import com.yapp.presentation.util.intent.intentToPlayStore

@OptIn(ExperimentalComposeUiApi::class)
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

                    is LoginUiSideEffect.NavigateToPlayStore -> {
                        intentToPlayStore(context)
                    }
                }
            }
        }

        ConstraintLayout(
            constraintSet = constraintSet(),
            modifier = Modifier
                .fillMaxSize()
                .background(AttendanceTheme.colors.backgroundColors.background)
                .systemBarsPadding()
                .padding(24.dp),
        ) {
            IntroduceTitle()
            KakaoLoginButton()

            if (uiState.isGuestButtonVisible) {
                GuestButton()
            }
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

    when (uiState.dialogState) {
        DialogState.NONE -> Unit

        DialogState.REQUIRE_UPDATE -> {
            YDSPopupDialog(
                title = stringResource(R.string.required_update_title),
                content = stringResource(R.string.required_update_content),
                positiveButtonText = stringResource(R.string.update_confirm),
                onClickPositiveButton = { viewModel.setEvent(LoginUiEvent.OnUpdateButtonClicked) },
                onDismiss = { }
            )
        }

        DialogState.NECESSARY_UPDATE -> {
            YDSPopupDialog(
                title = stringResource(R.string.necessary_update_title),
                content = stringResource(R.string.necessary_update_content),
                negativeButtonText = stringResource(R.string.update_cancel),
                positiveButtonText = stringResource(R.string.update_confirm),
                onClickPositiveButton = { viewModel.setEvent(LoginUiEvent.OnUpdateButtonClicked) },
                onClickNegativeButton = { viewModel.setEvent(LoginUiEvent.OnCancelButtonClicked) },
                onDismiss = { }
            )
        }

        DialogState.INSERT_CODE_NUMBER -> {
            var password by remember { mutableStateOf("") }
            YDSPopupDialog(
                title = stringResource(id = R.string.login_admin_title),
                modifier = Modifier.imePadding(),
                content = stringResource(id = R.string.login_admin_content),
                negativeButtonText = stringResource(id = R.string.Cancel),
                positiveButtonText = stringResource(id = R.string.Ok),
                onClickPositiveButton = { viewModel.adminLogin(password) },
                onClickNegativeButton = { viewModel.setEvent(LoginUiEvent.OnCancelButtonClicked) },
                editTextInitValue = password,
                editTextChangedListener = { password = it },
                editTextHint = stringResource(id = R.string.login_admin_password_placeholder),
                onDismiss = { }
            )
        }
    }
}

@Composable
private fun YappuImage(
    onClickImage: () -> Unit,
) {
    val composition: LottieCompositionResult =
        rememberLottieComposition(LottieCompositionSpec.RawRes(raw.login_buong))
    LottieAnimation(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            onClickImage.invoke()
        },
        composition = composition.value
    )
}

@Composable
private fun IntroduceTitle() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .layoutId("introduce"),
        text = stringResource(id = R.string.login_attendance_introduce_text),
        style = AttendanceTypography.h1,
        color = AttendanceTheme.colors.grayScale.Gray1200
    )
}

/**
 * 플레이 스토어 심사를 위한 게스트 버튼
 */
@Composable
private fun GuestButton(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    Button(
        modifier = Modifier
            .layoutId("guestButton")
            .fillMaxWidth()
            .height(45.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            viewModel.setEvent(LoginUiEvent.OnGuestButtonClicked)
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = AttendanceTheme.colors.mainColors.YappOrange
        ),
        elevation = null
    ) {
        Text(
            "Guest Login"
        )
    }
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
        val kakaoLoginButton = createRefFor("kakaoLoginButton")

        constrain(introduce) {
            start.linkTo(parent.start)
            bottom.linkTo(kakaoLoginButton.top, 60.dp)
            end.linkTo(parent.end)
        }

        constrain(kakaoLoginButton) {
            start.linkTo(parent.start)
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end)
        }
    }
}
