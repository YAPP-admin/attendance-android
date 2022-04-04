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
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoSdkProvider: KakaoSdkProviderInterface,
    private val setMemberIdUseCase: SetMemberIdUseCase
) :
    BaseViewModel<LoginUiState, LoginUiSideEffect, LoginUiEvent>(
        LoginUiState()
    ) {

    override fun handleEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnLoginButtonClicked -> {
                setState {
                    copy(isLoading = true)
                }
                kakaoSdkProvider.login(
                    onSuccess = {
                        kakaoSdkProvider.validateAccessToken(
                            onSuccess = { userAccountId ->
                                viewModelScope.launch { setMemberIdUseCase(userAccountId) }
                                setEffect(
                                    LoginUiSideEffect.NavigateToSignUpScreen,
                                    LoginUiSideEffect.ShowToast("로그인 성공")
                                )
                                setState {
                                    copy(isLoading = false)
                                }

                            },
                            onFailed = { setEffect(
                                LoginUiSideEffect.ShowToast("로그인 실패")
                            ) }
                        )


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
