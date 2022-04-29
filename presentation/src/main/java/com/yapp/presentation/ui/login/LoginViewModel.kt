package com.yapp.presentation.ui.login

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.common.util.KakaoSdkProvider
import com.yapp.domain.common.KakaoSdkProviderInterface
import com.yapp.domain.usecases.GetFirestoreMemberUseCase
import com.yapp.domain.usecases.SetMemberIdUseCase
import com.yapp.presentation.ui.login.LoginContract.*
import com.yapp.presentation.ui.splash.SplashContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * flow
 * 1. 앱 삭제 후 재설치를 하는 경우, 로그인 시 signup 건너뛰고 바로 메인화면으로 이동
 * 2. 최초 가입 시 signup 단계에서 도중에 앱을 종료하는 경우, 로그인 후 다시 signup 화면으로 도달
 * 3. 정상적으로 signup 까지 완료한 경우, 메인화면으로 이동
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val setMemberIdUseCase: SetMemberIdUseCase,
    private val getFirestoreMemberUseCase: GetFirestoreMemberUseCase,
    private val kakaoSdkProvider: KakaoSdkProviderInterface
) :
    BaseViewModel<LoginUiState, LoginUiSideEffect, LoginUiEvent>(
        LoginUiState()
    ) {

    companion object {
        const val HIDDEN_CONDITION = 15
    }

    private fun kakaoLogin() {
        kakaoSdkProvider.login(
            onSuccess = {
                validateKakaoToken()
                setState { copy(isLoading = false) }
            },
            onFailed = {
                setEffect(LoginUiSideEffect.ShowToast("로그인 실패"))
                setState { copy(isLoading = false) }
            }
        )
    }

    private fun validateKakaoToken() {
        kakaoSdkProvider.validateAccessToken(
            onSuccess = { userAccountId ->
                viewModelScope.launch {
                    setMemberId(userAccountId)
                    validateRegisteredUser()
                }
            },
            onFailed = {
                setEffect(LoginUiSideEffect.ShowToast("로그인 실패"))
                setState { copy(isLoading = false) }
            }
        )
    }

    private suspend fun validateRegisteredUser() {
        getFirestoreMemberUseCase().collectWithCallback(
            onSuccess = { entity ->
                // 이미 firebase 에 존재하는 유저인 경우,
                entity?.let {
                    // 바로 메인화면으로 이동한다.
                    setEffect(
                        LoginUiSideEffect.NavigateToQRMainScreen,
                    )
                } ?: run {
                    // 존재하지 않는다면, signup 화면으로 이동한다.
                    setEffect(
                        LoginUiSideEffect.NavigateToSignUpScreen,
                        LoginUiSideEffect.ShowToast("로그인 성공")
                    )
                }
            },
            onFailed = {
                setEffect(LoginUiSideEffect.ShowToast("로그인 실패"))
                setState { copy(isLoading = false) }
            }
        )
    }

    private suspend fun setMemberId(id: Long) {
        // user id 를 setting 할 때까지 coroutine scope 가 유지되어야 하므로,
        // withContext 로 선언
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

            is LoginUiEvent.OnYappuImageClicked -> {
                setState {
                    if(this.clickCount == HIDDEN_CONDITION) {
                        this.copy(clickCount = 0, isDialogVisible = true)
                    } else {
                        this.copy(clickCount = this.clickCount + 1)
                    }
                }
            }
        }
    }
}
