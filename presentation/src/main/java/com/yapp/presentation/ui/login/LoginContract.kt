package com.yapp.presentation.ui.login

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class LoginContract {
    data class LoginUiState(
        val isLoading: Boolean = false,
        val clickCount: Int = 0,
        val dialogState: DialogState = DialogState.NONE,
        val isGuestButtonVisible: Boolean = false,
    ) : UiState

    sealed class LoginUiSideEffect : UiSideEffect {
        object NavigateToQRMainScreen : LoginUiSideEffect()
        object NavigateToSignUpScreen : LoginUiSideEffect()
        object NavigateToAdminScreen : LoginUiSideEffect()
        object NavigateToPlayStore : LoginUiSideEffect()
        data class ShowToast(val msg: String) : LoginUiSideEffect()
        object ExitProcess : LoginUiSideEffect()
    }

    sealed class LoginUiEvent : UiEvent {
        object OnLoginButtonClicked : LoginUiEvent()
        object OnSkipButtonClicked : LoginUiEvent()
        object OnYappuImageClicked : LoginUiEvent()
        object OnUpdateButtonClicked : LoginUiEvent()
        object OnCancelButtonClicked : LoginUiEvent()
        object OnGuestButtonClicked : LoginUiEvent()
        object OnExitButtonClicked : LoginUiEvent()
    }

    // NONE = 다이얼로그 미출력
    enum class DialogState {
        NONE, REQUIRE_UPDATE, NECESSARY_UPDATE, FAIL_INIT_LOAD, INSERT_CODE_NUMBER
    }
}
