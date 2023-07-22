package com.yapp.presentation.ui

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class MainContract {
    data class MainUiState(
        val toastMessage: String = ""
    ) : UiState

    sealed class MainUiSideEffect : UiSideEffect {
        object ShowToast : MainUiSideEffect()
        object NavigateToQRScreen : MainUiSideEffect()
    }

    sealed class MainUiEvent : UiEvent {
        object OnClickQrAuthButton : MainUiEvent()
    }
}
