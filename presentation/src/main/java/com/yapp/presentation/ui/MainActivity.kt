package com.yapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.util.KakaoTalkLoginProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var kakaoTalkLoginProvider: KakaoTalkLoginProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AttendanceTheme {
                AttendanceScreen(kakaoTalkLoginProvider)
            }
        }
    }
}