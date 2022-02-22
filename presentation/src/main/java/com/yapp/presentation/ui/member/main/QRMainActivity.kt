package com.yapp.presentation.ui.member.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.yapp.common.theme.AttendanceTheme
import com.yapp.presentation.ui.login.LoginViewModel
import com.yapp.presentation.ui.member.AttendanceScreen
import com.yapp.presentation.ui.member.main.state.QRMainContract
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QRMainActivity : ComponentActivity() {
    private val viewModel: QRMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observing()

        setContent {
            AttendanceTheme {
                AttendanceScreen()
            }
        }
    }

    private fun observing() {
        lifecycleScope.launch {
            viewModel.effect.collect { effect ->
                when(effect) {
                    is QRMainContract.QRMainUiSideEffect.ShowToast -> {
                    }
                }
            }
        }
    }
}