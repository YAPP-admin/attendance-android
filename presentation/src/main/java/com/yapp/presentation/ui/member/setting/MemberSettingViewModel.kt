package com.yapp.presentation.ui.member.setting

import com.yapp.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MemberSettingViewModel @Inject constructor() :
    BaseViewModel<MemberSettingContract.MemberSettingUiState, MemberSettingContract.MemberSettingUiSideEffect, MemberSettingContract.MemberSettingUiEvent>(
        MemberSettingContract.MemberSettingUiState()
    ) {

    override fun handleEvent(event: MemberSettingContract.MemberSettingUiEvent) {
        when (event) {

        }
    }
}