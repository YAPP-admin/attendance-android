package com.yapp.presentation.ui.member.setting

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.domain.model.Team

class MemberSettingContract {
    data class MemberSettingUiState(
        val loadState: LoadState = LoadState.Idle,
        val showDialog: Boolean = false,
        val generation: Int = 0,
        val memberName: String = "",
        val memberPosition: String = "",
        val memberTeam: Team = Team.empty(),
        val isGuest: Boolean = false,
    ) : UiState

    enum class LoadState {
        Loading, Idle, Error
    }

    sealed class MemberSettingUiSideEffect : UiSideEffect {
        object NavigateToLoginScreen : MemberSettingUiSideEffect()
        object NavigateToPrivacyPolicyScreen : MemberSettingUiSideEffect()
        object NavigateToSelectTeamScreen : MemberSettingUiSideEffect()
        object ShowToast : MemberSettingUiSideEffect()
    }

    sealed class MemberSettingUiEvent : UiEvent {
        object OnLogoutButtonClicked : MemberSettingUiEvent()
        object OnWithdrawButtonClicked : MemberSettingUiEvent()
        object OnPrivacyPolicyButtonClicked : MemberSettingUiEvent()
        object OnSelectTeamButtonClicked : MemberSettingUiEvent()
    }
}
