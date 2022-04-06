package com.yapp.presentation.ui.member.setting

import android.util.Log
import com.kakao.sdk.user.UserApiClient
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.common.KakaoSdkProviderInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MemberSettingViewModel @Inject constructor(
    private val kakaoSdkProvider: KakaoSdkProviderInterface
) :
    BaseViewModel<MemberSettingContract.MemberSettingUiState, MemberSettingContract.MemberSettingUiSideEffect, MemberSettingContract.MemberSettingUiEvent>(
        MemberSettingContract.MemberSettingUiState()
    ) {

    private fun logout() {
        kakaoSdkProvider.logout(
            onSuccess = {
                setEffect(MemberSettingContract.MemberSettingUiSideEffect.NavigateToLoginScreen)
            },
            onFailed = {
                // 실패했다는 토스트
            }
        )

        // 카카오 로그인 외에, 저장된 모든 정보를 지우는 유즈케이스가 필요함.
    }

    override fun handleEvent(event: MemberSettingContract.MemberSettingUiEvent) {
        when (event) {
            is MemberSettingContract.MemberSettingUiEvent.OnLogoutButtonClicked -> {
                logout()
            }

            is MemberSettingContract.MemberSettingUiEvent.OnPrivacyPolicyButtonClicked -> {
                setEffect(MemberSettingContract.MemberSettingUiSideEffect.NavigateToPrivacyPolicyScreen)
            }
        }
    }
}