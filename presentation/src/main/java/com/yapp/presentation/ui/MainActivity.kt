package com.yapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.runtime.CompositionLocalProvider
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.util.KakaoTalkLoginProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var kakaoTalkLoginProvider: KakaoTalkLoginProvider

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AttendanceTheme {
                CompositionLocalProvider(
                    LocalOverScrollConfiguration provides null
                ) {
                    AttendanceScreen(
                        kakaoTalkLoginProvider
                    )
                }
            }
        }
    }
}