package com.yapp.presentation.ui.member.main

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class MemberMainContract {
    data class MemberMainUiState(
        val isLoading: Boolean = true,
        val showDialog: Boolean = false,
        val selectedTab: BottomNavigationItem = BottomNavigationItem.SESSION,
        val isAttendanceCompleted: Boolean = false,
    ) : UiState

    sealed class MemberMainUiSideEffect : UiSideEffect {
        class NavigateToRoute(val tab: BottomNavigationItem) : MemberMainUiSideEffect()
    }

    sealed class MemberMainUiEvent : UiEvent {
        data class OnClickBottomNavigationTab(val tab: BottomNavigationItem) : MemberMainUiEvent()
    }
}
