package com.yapp.presentation.ui.splash

import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.user.UserApiClient
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.common.KakaoSdkProviderInterface
import com.yapp.domain.usecases.SetMemberIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val kakaoSdkProvider: KakaoSdkProviderInterface,
    private val setMemberIdUseCase: SetMemberIdUseCase
) : BaseViewModel<SplashContract.SplashUiState, SplashContract.SplashUiSideEffect, SplashContract.SplashUiEvent>(
    SplashContract.SplashUiState()
) {
    init {
        kakaoSdkProvider.validateAccessToken(
            onSuccess = { userAccountId ->
                viewModelScope.launch { setMemberIdUseCase(userAccountId) }
                setState { copy(loginState = SplashContract.LoginState.SUCCESS) }
            },
            onFailed = {
                setState { copy(loginState = SplashContract.LoginState.REQUIRED) }
            }
        )
    }

    override fun setEvent(event: SplashContract.SplashUiEvent) {
        super.setEvent(event)
    }

    override fun handleEvent(event: SplashContract.SplashUiEvent) {
        TODO("Not yet implemented")
    }
}
