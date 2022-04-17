package com.yapp.presentation.ui.login

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.common.util.KakaoSdkProvider
import com.yapp.domain.common.KakaoSdkProviderInterface
import com.yapp.domain.usecases.SetMemberIdUseCase
import com.yapp.presentation.ui.login.LoginContract.*
import com.yapp.presentation.ui.splash.SplashContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val setMemberIdUseCase: SetMemberIdUseCase,
    private val kakaoSdkProvider: KakaoSdkProviderInterface
) :
    BaseViewModel<LoginUiState, LoginUiSideEffect, LoginUiEvent>(
        LoginUiState()
    ) {

    private fun kakaoLogin() {
        kakaoSdkProvider.login(
            onSuccess = {
                validateToken()
                setEffect(
                    LoginUiSideEffect.NavigateToSignUpScreen,
                    LoginUiSideEffect.ShowToast("로그인 성공")
                )
                setState { copy(isLoading = false) }
            },
            onFailed = {
                setEffect(LoginUiSideEffect.ShowToast("로그인 실패"))
                setState { copy(isLoading = false) }
            }
        )
    }

    private fun validateToken() {
        kakaoSdkProvider.validateAccessToken(
            onSuccess = { userAccountId ->
                viewModelScope.launch {
                    setMemberId(userAccountId)
                }
            },
            onFailed = {
                setEffect(LoginUiSideEffect.ShowToast("로그인 실패"))
                setState { copy(isLoading = false) }
            }
        )
    }

    private suspend fun setMemberId(id: Long) {
        withContext(viewModelScope.coroutineContext) {
            setMemberIdUseCase(id)
        }
    }

    override suspend fun handleEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnLoginButtonClicked -> {
                setState { copy(isLoading = true) }
                kakaoLogin()
            }

            is LoginUiEvent.OnSkipButtonClicked -> {
                setEffect(
                    LoginUiSideEffect.NavigateToQRMainScreen
                )
            }
        }
    }
}
