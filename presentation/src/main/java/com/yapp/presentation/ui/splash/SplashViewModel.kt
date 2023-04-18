package com.yapp.presentation.ui.splash

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.common.KakaoSdkProviderInterface
import com.yapp.domain.usecases.GetCurrentMemberInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val kakaoSdkProvider: KakaoSdkProviderInterface,
    private val getCurrentMemberInfoUseCase: GetCurrentMemberInfoUseCase
) : BaseViewModel<SplashContract.SplashUiState, SplashContract.SplashUiSideEffect, SplashContract.SplashUiEvent>(
    SplashContract.SplashUiState()
) {
    init {
        kakaoSdkProvider.validateAccessToken(
            onSuccess = {
                viewModelScope.launch {
                    validateRegisteredUser()
                }
            },
            onFailed = {
                setState { copy(loginState = SplashContract.LoginState.REQUIRED) }
            }
        )
    }

    private suspend fun validateRegisteredUser() {
        getCurrentMemberInfoUseCase()
            .onSuccess { currentMember ->
                currentMember?.let {
                    setState { copy(loginState = SplashContract.LoginState.SUCCESS) }
                } ?: run {
                    setState { copy(loginState = SplashContract.LoginState.REQUIRED) }
                }
            }
            .onFailure {
                setState { copy(loginState = SplashContract.LoginState.REQUIRED) }
            }
    }

    override suspend fun handleEvent(event: SplashContract.SplashUiEvent) = Unit

}
