package com.yapp.presentation.ui.login

import com.yapp.common.base.BaseViewModel
import com.yapp.presentation.ui.login.state.LoginContract
import com.yapp.presentation.ui.login.state.LoginContract.*

class LoginViewModel : BaseViewModel<LoginUiState, LoginUiSideEffect, LoginUiEvent>(
    LoginUiState()) {

    override fun handleEvent(event: LoginUiEvent) {
        when(event) {
            is LoginUiEvent.OnLoginButtonClicked -> {
                setEffect { LoginUiSideEffect.ShowKakaoTalkLoginPage }
            }
        }
    }
}
