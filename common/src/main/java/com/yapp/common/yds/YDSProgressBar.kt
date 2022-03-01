package com.yapp.common.yds

import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp

@Composable
fun YDSProgressBar() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(30.dp)
            .layoutId("progressBar"),
        strokeWidth = 3.dp
    )
}
