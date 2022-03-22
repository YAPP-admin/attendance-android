package com.yapp.presentation.ui.member.setting

import android.util.Log
import com.kakao.sdk.user.UserApiClient
import com.yapp.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MemberSettingViewModel @Inject constructor() :
    BaseViewModel<MemberSettingContract.MemberSettingUiState, MemberSettingContract.MemberSettingUiSideEffect, MemberSettingContract.MemberSettingUiEvent>(
        MemberSettingContract.MemberSettingUiState()
    ) {

    private fun logout() {
        // 카카오톡 로그아웃, 카카오 로직을 묶을 필요가 있을 듯
        UserApiClient.instance.logout { error ->
            error?.let {
                // 실패했다는 토스트
            } ?: run {
                setEffect(MemberSettingContract.MemberSettingUiSideEffect.NavigateToLoginScreen)
            }
        }

        // 카카오 로그인 외에, 저장된 모든 정보를 지우는 유즈케이스가 필요함.
    }

    override fun handleEvent(event: MemberSettingContract.MemberSettingUiEvent) {
        when (event) {
            is MemberSettingContract.MemberSettingUiEvent.OnLogoutButtonClicked -> {
                logout()
            }
        }
    }
}