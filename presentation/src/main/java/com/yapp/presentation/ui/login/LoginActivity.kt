package com.yapp.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.yapp.common.util.KakaoTalkLoginProvider
import com.yapp.presentation.ui.login.screen.Login
import com.yapp.presentation.ui.login.state.LoginContract
import com.yapp.presentation.ui.member.main.QRMainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    @Inject
    lateinit var kakaoTalkLoginProvider: KakaoTalkLoginProvider

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initKakaoLogin(kakaoTalkLoginProvider)
        observing()

        setContent {
            Login()
        }
    }

    private fun observing() {
        lifecycleScope.launch {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is LoginContract.LoginUiSideEffect.ShowToast -> {
                        Toast.makeText(this@LoginActivity, effect.msg, Toast.LENGTH_SHORT).show()
                    }
                    is LoginContract.LoginUiSideEffect.NavigateToQRMainScreen -> {
                        startActivity(
                            Intent(this@LoginActivity, QRMainActivity::class.java)
                        )
                    }
                }
            }
        }
    }
}
