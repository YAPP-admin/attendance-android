package com.yapp.presentation.ui.member.main

import com.yapp.common.base.BaseViewModel
import com.yapp.presentation.ui.member.main.MemberMainContract.MemberMainUiEvent
import com.yapp.presentation.ui.member.main.MemberMainContract.MemberMainUiSideEffect
import com.yapp.presentation.ui.member.main.MemberMainContract.MemberMainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MemberMainViewModel @Inject constructor() :
    BaseViewModel<MemberMainUiState, MemberMainUiSideEffect, MemberMainUiEvent>(MemberMainUiState()) {

    override suspend fun handleEvent(event: MemberMainUiEvent) {
        when (event) {
            is MemberMainUiEvent.OnClickBottomNavigationTab -> {
                setState { copy(selectedTab = event.tab) }
                setEffect( MemberMainUiSideEffect.NavigateToRoute(event.tab))
            }
        }
    }

}

