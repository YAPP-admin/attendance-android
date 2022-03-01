package com.yapp.presentation.ui.splash

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class SplashContract {
    data class SplashUiState(
        val loginState: LoginState = LoginState.NONE
    ) : UiState

    sealed class SplashUiSideEffect : UiSideEffect {
    }

    sealed class SplashUiEvent : UiEvent {
    }

    enum class LoginState {
        NONE, SUCCESS, REQUIRED
    }
}
