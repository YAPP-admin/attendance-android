package com.yapp.presentation.ui.login

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class LoginContract {
    data class LoginUiState(
        val isLoading: Boolean = false,
    ) : UiState

    sealed class LoginUiSideEffect : UiSideEffect {
        object NavigateToQRMainScreen : LoginUiSideEffect()
        object NavigateToSignUpScreen : LoginUiSideEffect()
        data class ShowToast(val msg: String) : LoginUiSideEffect()
    }

    sealed class LoginUiEvent : UiEvent {
        object OnLoginButtonClicked : LoginUiEvent()
        object OnSkipButtonClicked : LoginUiEvent()
    }
}
