package com.yapp.common.yds

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yapp.common.R
import com.yapp.common.theme.AttendanceTheme

@Composable
fun YDSProgressBar() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AttendanceTheme.colors.backgroundColors.background.copy(alpha = 0.6f)
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.progress_bar_loop))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(composition, { progress })
    }
}
