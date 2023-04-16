package com.yapp.presentation.ui.member.setting

import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.common.KakaoSdkProviderInterface
import com.yapp.domain.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberSettingViewModel @Inject constructor(
    private val kakaoSdkProvider: KakaoSdkProviderInterface,
    private val getMemberIdUseCase: GetMemberIdUseCase,
    private val getCurrentMemberInfoUseCase: GetCurrentMemberInfoUseCase,
    private val deleteMemberInfoUseCase: DeleteMemberInfoUseCase,
    private val getConfigUseCase: GetConfigUseCase,
    private val shouldShowGuestButtonUseCase: ShouldShowGuestButtonUseCase,
) :
    BaseViewModel<MemberSettingContract.MemberSettingUiState, MemberSettingContract.MemberSettingUiSideEffect, MemberSettingContract.MemberSettingUiEvent>(
        MemberSettingContract.MemberSettingUiState()
    ) {

    init {
        viewModelScope.launch {
            setState { copy(loadState = MemberSettingContract.LoadState.Loading) }
            getCurrentMemberInfoUseCase()
                .onSuccess { currentMember ->
                    setState {
                        copy(
                            loadState = MemberSettingContract.LoadState.Idle,
                            memberName = currentMember?.name ?: ""
                        )
                    }
                }.onFailure {
                    setState { copy(loadState = MemberSettingContract.LoadState.Error) }
                }

            getConfigUseCase()
                .onSuccess { config ->
                    setState {
                        copy(
                            loadState = MemberSettingContract.LoadState.Idle,
                            generation = config.generation
                        )
                    }
                }
                .onFailure {
                    setState { copy(loadState = MemberSettingContract.LoadState.Error) }
                }

            shouldShowGuestButtonUseCase()
                .onSuccess { setState { copy(isGuest = it) } }
                .onFailure { FirebaseCrashlytics.getInstance().recordException(it) }
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
            getMemberIdUseCase()
                .onSuccess { memberId ->
                    require(memberId != null)

                    deleteMemberInfoUseCase(memberId).getOrDefault(defaultValue = false).also { isSuccess ->
                        if (!isSuccess) {
                            setState { copy(loadState = MemberSettingContract.LoadState.Idle) }
                            setEffect(MemberSettingContract.MemberSettingUiSideEffect.ShowToast)
                        }

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
                    }
                }
                .onFailure {
                    setState { copy(loadState = MemberSettingContract.LoadState.Idle) }
                    setEffect(MemberSettingContract.MemberSettingUiSideEffect.ShowToast)
                }
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
            getMemberIdUseCase()
                .onSuccess { memberId ->
                    require(memberId != null)
                    deleteMemberInfoUseCase(memberId).getOrDefault(false).also { isSuccess ->
                        if (!isSuccess) {
                            setState { copy(loadState = MemberSettingContract.LoadState.Idle) }
                            setEffect(MemberSettingContract.MemberSettingUiSideEffect.ShowToast)
                            return@onSuccess
                        }

                        setState { copy(loadState = MemberSettingContract.LoadState.Idle) }
                        setEffect(MemberSettingContract.MemberSettingUiSideEffect.NavigateToLoginScreen)
                    }

                }.onFailure {
                    setState { copy(loadState = MemberSettingContract.LoadState.Idle) }
                    setEffect(MemberSettingContract.MemberSettingUiSideEffect.ShowToast)
                }
        }
    }
}

