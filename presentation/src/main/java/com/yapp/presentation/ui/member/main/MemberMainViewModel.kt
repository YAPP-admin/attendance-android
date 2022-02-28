package com.yapp.presentation.ui.member.main

import com.yapp.common.base.BaseViewModel
import com.yapp.domain.util.firebase.FirebaseRemoteConfig
import com.yapp.domain.util.firebase.RemoteConfigData
import com.yapp.presentation.ui.member.main.MemberMainContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MemberMainViewModel @Inject constructor() :
    BaseViewModel<MemberMainUiState, MemberMainUiSideEffect, MemberMainUiEvent>(MemberMainUiState()) {

    override fun handleEvent(event: MemberMainUiEvent) {
        when (event) {
            is MemberMainUiEvent.OnClickBottomNavigationTab -> {
                setState { copy(selectedTab = event.tab) }
            }
        }
    }

}

