package com.yapp.presentation.ui.member.signup.name

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class NameContract {
    data class NameUiState(
        val showDialog: Boolean = false,
        val isLoading: Boolean = false,
        val name: String = ""
    ) : UiState

    sealed class NameSideEffect : UiSideEffect {
        object NavigateToPreviousScreen : NameSideEffect()
        data class NavigateToNextScreen(val name: String) : NameSideEffect()
    }

    sealed class NameUiEvent : UiEvent {
        data class InputName(val name: String) : NameUiEvent()
        object OnCancelButtonClick : NameUiEvent()
        object OnNextButtonClick : NameUiEvent()
        object OnBackButtonClick : NameUiEvent()
        object OnDismissDialogButtonClick : NameUiEvent()
    }
}
