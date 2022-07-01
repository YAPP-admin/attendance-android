package com.yapp.common.yds

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.*
import com.yapp.common.R

@Composable
fun YDSProgressBar() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White.copy(alpha = 0.6f)
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.progress_bar_loop))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(composition, progress)
    }
}
