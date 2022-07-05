package com.yapp.presentation.ui.member.setting

import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.common.KakaoSdkProviderInterface
import com.yapp.domain.usecases.*
import com.yapp.presentation.model.Config.Companion.mapTo
import com.yapp.presentation.model.Member.Companion.mapTo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberSettingViewModel @Inject constructor(
    private val kakaoSdkProvider: KakaoSdkProviderInterface,
    private val getMemberIdUseCase: GetMemberIdUseCase,
    private val getFirestoreMemberUseCase: GetFirestoreMemberUseCase,
    private val deleteMemberInfoUseCase: DeleteMemberInfoUseCase,
    private val getConfigUseCase: GetConfigUseCase,
    private val shouldShowGuestButtonUseCase: ShouldShowGuestButtonUseCase
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
                        copy(
                            loadState = MemberSettingContract.LoadState.Idle,
                            memberName = it?.mapTo()?.name ?: ""
                        )
                    }
                },
                onFailed = {
                    setState { copy(loadState = MemberSettingContract.LoadState.Error) }
                }
            )

            getConfigUseCase().collectWithCallback(
                onSuccess = {
                    setState {
                        copy(
                            loadState = MemberSettingContract.LoadState.Idle,
                            generation = it.mapTo().generation
                        )
                    }
                },
                onFailed = {
                    setState { copy(loadState = MemberSettingContract.LoadState.Error) }
                }
            )

            shouldShowGuestButtonUseCase().collectWithCallback(
                onSuccess = {
                    setState { copy(isGuest = it) }
                },
                onFailed = {
                    FirebaseCrashlytics.getInstance().recordException(it)
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
                                        setState { copy(loadState = MemberSettingContract.LoadState.Idle) }
                                        setEffect(MemberSettingContract.MemberSettingUiSideEffect.NavigateToLoginScreen)
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
                if (uiState.value.isGuest) guestLogout()
                else logout()
            }

            is MemberSettingContract.MemberSettingUiEvent.OnWithdrawButtonClicked -> {
                if (uiState.value.isGuest) guestWithdraw()
                else withdraw()
            }

            is MemberSettingContract.MemberSettingUiEvent.OnPrivacyPolicyButtonClicked -> {
                setEffect(MemberSettingContract.MemberSettingUiSideEffect.NavigateToPrivacyPolicyScreen)
            }
        }
    }

    // 심사 후 제거할 로직이라 아래에 선언함 !
    // 심사를 위한 게스트용 로그아웃, 탈퇴기능
    private fun guestLogout() {
        setEffect(MemberSettingContract.MemberSettingUiSideEffect.NavigateToLoginScreen)
    }

    private fun guestWithdraw() {
        viewModelScope.launch {
            setState { copy(loadState = MemberSettingContract.LoadState.Loading) }
            getMemberIdUseCase().collectWithCallback(
                onSuccess = { memberId ->
                    deleteMemberInfoUseCase(memberId)
                        .collect { isSucceed ->
                            if (isSucceed) {
                                setState { copy(loadState = MemberSettingContract.LoadState.Idle) }
                                setEffect(MemberSettingContract.MemberSettingUiSideEffect.NavigateToLoginScreen)
                            } else {
                                setState { copy(loadState = MemberSettingContract.LoadState.Idle) }
                                setEffect(MemberSettingContract.MemberSettingUiSideEffect.ShowToast)
                            }
                        }
                },
                onFailed = {
                    setState { copy(loadState = MemberSettingContract.LoadState.Idle) }
                    setEffect(MemberSettingContract.MemberSettingUiSideEffect.ShowToast)
                })
        }
    }
}

