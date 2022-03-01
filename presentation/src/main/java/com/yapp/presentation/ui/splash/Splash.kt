package com.yapp.presentation.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yapp.common.R
import com.yapp.common.theme.Yapp_Orange
import com.yapp.presentation.ui.splash.SplashContract.*

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
        color = Yapp_Orange
    ) {
        when (uiState.loginState) {
            LoginState.NONE -> {
                Image(
                    painter = painterResource(id = R.drawable.icon_splash),
                    contentDescription = null,
                )
            }
            else -> {
                SplashLoader(
                    uiState.loginState,
                    navigateToLogin,
                    navigateToMain
                )
            }
        }
    }
}

@Composable
fun SplashLoader(loginState: LoginState, navigateToLogin: () -> Unit, navigateToMain: () -> Unit) {
    val composition: LottieCompositionResult =
        when (loginState) {
            LoginState.SUCCESS -> rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.slpash_buong))
            else -> rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_buong_move))
        }

    val progress by animateLottieCompositionAsState(
        composition.value,
        iterations = 1
    )

    LaunchedEffect(progress) {
        if (progress == 1f) {
            when (loginState) {
                LoginState.SUCCESS -> navigateToMain()
                LoginState.REQUIRED -> navigateToLogin()
            }
        }
    }

    LottieAnimation(
        composition.value,
        progress,
    )
}