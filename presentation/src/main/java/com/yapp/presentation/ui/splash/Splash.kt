package com.yapp.presentation.ui.splash

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yapp.common.R
import com.yapp.common.theme.AttendanceTheme
import com.yapp.presentation.ui.splash.SplashContract.LoginState

@Composable
fun Splash(
    viewModel: SplashViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit,
    navigateToMain: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = AttendanceTheme.colors.mainColors.YappOrange
    ) {
        SplashLoader(
            uiState.loginState,
            navigateToLogin,
            navigateToMain
        )
    }
}

@Composable
fun SplashLoader(loginState: LoginState, navigateToLogin: () -> Unit, navigateToMain: () -> Unit) {
    val composition: LottieCompositionResult? =
        when (loginState) {
            LoginState.SUCCESS -> rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.slpash_buong))
            LoginState.REQUIRED -> rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_buong_move))
            else -> null
        }

    val progress by animateLottieCompositionAsState(
        composition?.value,
        iterations = 1
    )

    LaunchedEffect(progress) {
        if (progress == 1f) {
            when (loginState) {
                LoginState.SUCCESS -> navigateToMain()
                LoginState.REQUIRED -> navigateToLogin()
                LoginState.NONE -> Unit
            }
        }
    }

    LottieAnimation(
        composition?.value,
        { progress },
    )
}