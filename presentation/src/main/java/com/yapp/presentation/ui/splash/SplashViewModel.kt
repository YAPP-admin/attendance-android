package com.yapp.presentation.ui.splash

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.common.KakaoSdkProviderInterface
import com.yapp.domain.usecases.GetFirestoreMemberUseCase
import com.yapp.domain.usecases.SetMemberIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val kakaoSdkProvider: KakaoSdkProviderInterface,
    private val getFirestoreMemberUseCase: GetFirestoreMemberUseCase
) : BaseViewModel<SplashContract.SplashUiState, SplashContract.SplashUiSideEffect, SplashContract.SplashUiEvent>(
    SplashContract.SplashUiState()
) {
    init {
        kakaoSdkProvider.validateAccessToken(
            onSuccess = { userAccountId ->
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
        getFirestoreMemberUseCase().collectWithCallback(
            onSuccess = { entity ->
                entity?.let {
                    setState { copy(loginState = SplashContract.LoginState.SUCCESS) }
                } ?: run {
                    setState { copy(loginState = SplashContract.LoginState.REQUIRED) }
                }
            },
            onFailed = {
                setState { copy(loginState = SplashContract.LoginState.REQUIRED) }
            }
        )
    }

    override fun setEvent(event: SplashContract.SplashUiEvent) {
        super.setEvent(event)
    }

    override suspend fun handleEvent(event: SplashContract.SplashUiEvent) {
        TODO("Not yet implemented")
    }
}
