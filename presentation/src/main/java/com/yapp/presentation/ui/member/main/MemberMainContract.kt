package com.yapp.presentation.ui.member.main

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class MemberMainContract {
    data class MemberMainUiState(
        val isLoading: Boolean = true,
        val isShowTeamDialog: Boolean = false,
        val selectedTab: BottomNavigationItem = BottomNavigationItem.SESSION,
        val isAttendanceCompleted: Boolean = false,
    ) : UiState

    sealed class MemberMainUiSideEffect : UiSideEffect {
        data class NavigateToRoute(val tab: BottomNavigationItem) : MemberMainUiSideEffect()
        object NavigateToTeam : MemberMainUiSideEffect()
    }

    sealed class MemberMainUiEvent : UiEvent {
        data class OnClickBottomNavigationTab(val tab: BottomNavigationItem) : MemberMainUiEvent()
        object OnNextTime : MemberMainUiEvent()
        object OnSelectTeamScreen : MemberMainUiEvent()
    }
}
