package com.yapp.presentation.ui.member.setting

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class MemberSettingContract {
    data class MemberSettingUiState(
        val isLoading: Boolean = false,
        val showDialog: Boolean = false,
        val generation: Int = 0,
        val memberName: String = "",
    ) : UiState

    sealed class MemberSettingUiSideEffect : UiSideEffect {
        object NavigateToLoginScreen : MemberSettingUiSideEffect()
        object NavigateToPrivacyPolicyScreen : MemberSettingUiSideEffect()
    }

    sealed class MemberSettingUiEvent : UiEvent {
        object OnLogoutButtonClicked : MemberSettingUiEvent()
        object OnWithdrawButtonClicked : MemberSettingUiEvent()
        object OnPrivacyPolicyButtonClicked : MemberSettingUiEvent()
    }
}