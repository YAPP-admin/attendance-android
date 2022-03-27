package com.yapp.presentation.ui.member.setting

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class MemberSettingContract {
    data class MemberSettingUiState(
        val isLoading: Boolean = true,
        val showDialog: Boolean = false,
    ) : UiState

    sealed class MemberSettingUiSideEffect : UiSideEffect {
        object NavigateToLoginScreen: MemberSettingUiSideEffect()
    }

    sealed class MemberSettingUiEvent : UiEvent {
        object OnLogoutButtonClicked: MemberSettingUiEvent()
    }
}