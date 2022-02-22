package com.yapp.presentation.ui.login

import com.yapp.common.base.BaseViewModel
import com.yapp.common.util.KakaoTalkLoginProvider
import com.yapp.presentation.ui.login.LoginContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() :
    BaseViewModel<LoginUiState, LoginUiSideEffect, LoginUiEvent>(
        LoginUiState()
    ) {
    private var kakaoTalkLoginProvider: KakaoTalkLoginProvider? = null
    fun initKakaoLogin(provider: KakaoTalkLoginProvider) {
        kakaoTalkLoginProvider = provider
    }

    override fun handleEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnLoginButtonClicked -> {
                setState {
                    copy(isLoading = true)
                }
                kakaoTalkLoginProvider?.login(
                    onSuccess = {
                        setEffect(
                            LoginUiSideEffect.NavigateToQRMainScreen,
                            LoginUiSideEffect.ShowToast("로그인 성공")
                        )
                        setState {
                            copy(isLoading = false)
                        }
                    },
                    onFailed = {
                        setEffect(LoginUiSideEffect.ShowToast("로그인 실패"))
                        setState {
                            copy(isLoading = false)
                        }
                    }
                )
            }

            is LoginUiEvent.OnSkipButtonClicked -> {
                setEffect(
                    LoginUiSideEffect.NavigateToQRMainScreen
                )
            }
        }
    }
}
