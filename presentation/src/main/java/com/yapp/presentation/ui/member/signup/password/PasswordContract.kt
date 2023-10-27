package com.yapp.presentation.ui.member.signup.password

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

internal class PasswordContract {
    data class PasswordUiState(
        val isError: Boolean = false,
        val correctPassword: String = "",
        val inputPassword: String = "",
        val nextButtonEnabled: Boolean = false,
        val isWrong: Boolean = false,
    ) : UiState {
        companion object {
            const val PasswordDigit = 4
        }
    }

    sealed class PasswordSideEffect : UiSideEffect {
        object NavigateToPreviousScreen : PasswordSideEffect()
        object NavigateToNextScreen : PasswordSideEffect()
        object KeyboardHide : PasswordSideEffect()
        class ShowToast(val message: String) : PasswordSideEffect()
    }

    sealed class PasswordUiEvent : UiEvent {
        data class InputPassword(val password: String) : PasswordUiEvent()
        class OnNextButtonClick(val type: PasswordType) : PasswordUiEvent()
        object OnBackButtonClick : PasswordUiEvent()
    }
}
