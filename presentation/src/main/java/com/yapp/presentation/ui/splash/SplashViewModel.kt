package com.yapp.presentation.ui.splash

import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.user.UserApiClient
import com.yapp.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
) : BaseViewModel<SplashContract.SplashUiState, SplashContract.SplashUiSideEffect, SplashContract.SplashUiEvent>(
    SplashContract.SplashUiState()
) {
    init {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error == null) {
                    setState { copy(loginState = SplashContract.LoginState.SUCCESS) }

                } else {
                    setState { copy(loginState = SplashContract.LoginState.REQUIRED) }
                }
            }
        } else {
            setState { copy(loginState = SplashContract.LoginState.REQUIRED) }
        }
    }

    override fun setEvent(event: SplashContract.SplashUiEvent) {
        super.setEvent(event)
    }

    override fun handleEvent(event: SplashContract.SplashUiEvent) {
        TODO("Not yet implemented")
    }
}
