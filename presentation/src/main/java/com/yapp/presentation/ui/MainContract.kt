package com.yapp.presentation.ui

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.model.Session
import com.yapp.presentation.ui.member.setting.MemberSettingContract

class MainContract {
    object MainUiState : UiState

    sealed class MainUiSideEffect : UiSideEffect {
        object ShowToast : MainUiSideEffect()
        object NavigateToQRScreen : MainUiSideEffect()
    }

    sealed class MainUiEvent : UiEvent {
    }
}
