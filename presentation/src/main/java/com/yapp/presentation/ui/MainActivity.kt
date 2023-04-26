package com.yapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.yapp.common.theme.AttendanceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AttendanceTheme {
                CompositionLocalProvider(
                    LocalOverscrollConfiguration provides null
                ) {
                    ProvideWindowInsets (windowInsetsAnimationsEnabled = true){
                        AttendanceScreen()
                    }
                }
            }
        }
    }
}