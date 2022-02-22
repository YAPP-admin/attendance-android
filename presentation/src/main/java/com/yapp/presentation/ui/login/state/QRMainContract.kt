package com.yapp.presentation.ui.login.state

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.ui.login.state.LoginContract.*
import com.yapp.presentation.ui.member.main.state.QRMainContract

class LoginContract {
    data class LoginUiState(
        val isLoading: Boolean = false,
    ) : UiState

    sealed class LoginUiSideEffect : UiSideEffect {
        object StartQRMainScreen : LoginUiSideEffect()
        data class ShowToast(val msg: String) : LoginUiSideEffect()
    }

    sealed class LoginUiEvent : UiEvent {
        object OnLoginButtonClicked : LoginUiEvent()
        object OnSkipButtonClicked : LoginUiEvent()
    }
}
