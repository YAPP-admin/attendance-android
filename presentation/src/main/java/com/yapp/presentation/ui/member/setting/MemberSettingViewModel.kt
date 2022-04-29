package com.yapp.presentation.ui.member.setting

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.common.KakaoSdkProviderInterface
import com.yapp.domain.usecases.DeleteMemberInfoUseCase
import com.yapp.domain.usecases.GetConfigUseCase
import com.yapp.domain.usecases.GetFirestoreMemberUseCase
import com.yapp.domain.usecases.GetMemberIdUseCase
import com.yapp.presentation.model.Config.Companion.mapTo
import com.yapp.presentation.model.Member.Companion.mapTo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberSettingViewModel @Inject constructor(
    private val kakaoSdkProvider: KakaoSdkProviderInterface,
    private val getMemberIdUseCase: GetMemberIdUseCase,
    private val getFirestoreMemberUseCase: GetFirestoreMemberUseCase,
    private val deleteMemberInfoUseCase: DeleteMemberInfoUseCase,
    private val getConfigUseCase: GetConfigUseCase,
) :
    BaseViewModel<MemberSettingContract.MemberSettingUiState, MemberSettingContract.MemberSettingUiSideEffect, MemberSettingContract.MemberSettingUiEvent>(
        MemberSettingContract.MemberSettingUiState()
    ) {

    init {
        viewModelScope.launch {
            setState { copy(loadState = MemberSettingContract.LoadState.Loading) }
            getFirestoreMemberUseCase().collectWithCallback(
                onSuccess = {
                    setState {
                        copy(loadState = MemberSettingContract.LoadState.Idle,
                            memberName = it?.mapTo()?.name ?: "")
                    }
                },
                onFailed = {
                    setState { copy(loadState = MemberSettingContract.LoadState.Error) }
                }
            )
        }

        viewModelScope.launch {
            setState { copy(loadState = MemberSettingContract.LoadState.Loading) }
            getConfigUseCase().collectWithCallback(
                onSuccess = {
                    setState {
                        copy(loadState = MemberSettingContract.LoadState.Idle,
                            generation = it.mapTo().generation)
                    }
                },
                onFailed = {
                    setState { copy(loadState = MemberSettingContract.LoadState.Error) }
                }
            )
        }
    }

    private fun logout() {
        kakaoSdkProvider.logout(
            onSuccess = {
                setEffect(MemberSettingContract.MemberSettingUiSideEffect.NavigateToLoginScreen)
            },
            onFailed = {
                setEffect(MemberSettingContract.MemberSettingUiSideEffect.ShowToast)
            }
        )
    }

    private fun withdraw() {
        viewModelScope.launch {
            setState { copy(loadState = MemberSettingContract.LoadState.Loading) }
            getMemberIdUseCase().collectWithCallback(
                onSuccess = { memberId ->
                    deleteMemberInfoUseCase(memberId)
                        .collect { isSucceed ->
                            if (isSucceed) {
                                kakaoSdkProvider.withdraw(
                                    onSuccess = {
                                        setEffect(MemberSettingContract.MemberSettingUiSideEffect.NavigateToLoginScreen)
                                        setState { copy(loadState = MemberSettingContract.LoadState.Idle) }
                                    },
                                    onFailed = {
                                        setState { copy(loadState = MemberSettingContract.LoadState.Idle) }
                                        setEffect(MemberSettingContract.MemberSettingUiSideEffect.ShowToast)
                                    }
                                )
                            } else {
                                setState { copy(loadState = MemberSettingContract.LoadState.Idle) }
                                setEffect(MemberSettingContract.MemberSettingUiSideEffect.ShowToast)
                            }
                        }
                },
                onFailed = {
                    setState { copy(loadState = MemberSettingContract.LoadState.Idle) }
                    setEffect(MemberSettingContract.MemberSettingUiSideEffect.ShowToast)
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