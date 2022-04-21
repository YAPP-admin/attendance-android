package com.yapp.presentation.ui.member.setting

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.common.KakaoSdkProviderInterface
import com.yapp.domain.usecases.DeleteMemberInfoUseCase
import com.yapp.domain.usecases.GetMemberIdUseCase
import com.yapp.domain.usecases.SetMemberIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberSettingViewModel @Inject constructor(
    private val kakaoSdkProvider: KakaoSdkProviderInterface,
    private val getMemberIdUseCase: GetMemberIdUseCase,
    private val deleteMemberInfoUseCase: DeleteMemberInfoUseCase
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
    }

    private fun withdraw() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            getMemberIdUseCase().collectWithCallback(
                onSuccess = { memberId ->
                    deleteMemberInfoUseCase(memberId).zip(kakaoSdkProvider.withdraw())
                    { deleteSucceed, withdrawSucceed ->
                        withdrawSucceed && deleteSucceed
                    }
                        .collect { isSucceed ->
                            if (isSucceed) {
                                setEffect(MemberSettingContract.MemberSettingUiSideEffect.NavigateToLoginScreen)
                                setState { copy(isLoading = false) }
                            } else {
                                setState { copy(isLoading = false) }
                            }
                        }
                },
                onFailed = {
                    setState { copy(isLoading = false) }
                    // 실패했다는 토스트
                }
            )

        }
    }

    override suspend fun handleEvent(event: MemberSettingContract.MemberSettingUiEvent) {
        when (event) {
            is MemberSettingContract.MemberSettingUiEvent.OnLogoutButtonClicked -> {
                logout()
            }

            is MemberSettingContract.MemberSettingUiEvent.OnWithdrawButtonClicked -> {
                withdraw()
            }

            is MemberSettingContract.MemberSettingUiEvent.OnPrivacyPolicyButtonClicked -> {
                setEffect(MemberSettingContract.MemberSettingUiSideEffect.NavigateToPrivacyPolicyScreen)
            }
        }
    }
}