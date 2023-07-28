package com.yapp.presentation.ui.login

import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.common.KakaoSdkProviderInterface
import com.yapp.domain.model.types.VersionType
import com.yapp.domain.usecases.CheckAdminPasswordUseCase
import com.yapp.domain.usecases.CheckVersionUpdateUseCase
import com.yapp.domain.usecases.GetCurrentMemberInfoUseCase
import com.yapp.domain.usecases.SetMemberIdUseCase
import com.yapp.domain.usecases.ShouldShowGuestButtonUseCase
import com.yapp.presentation.ui.login.LoginContract.DialogState.FAIL_INIT_LOAD
import com.yapp.presentation.ui.login.LoginContract.DialogState.NECESSARY_UPDATE
import com.yapp.presentation.ui.login.LoginContract.DialogState.NONE
import com.yapp.presentation.ui.login.LoginContract.DialogState.REQUIRE_UPDATE
import com.yapp.presentation.ui.login.LoginContract.LoginUiEvent
import com.yapp.presentation.ui.login.LoginContract.LoginUiSideEffect
import com.yapp.presentation.ui.login.LoginContract.LoginUiState
import com.yapp.presentation.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

/**
 * flow
 * 1. 앱 삭제 후 재설치를 하는 경우, 로그인 시 signup 건너뛰고 바로 메인화면으로 이동
 * 2. 최초 가입 시 signup 단계에서 도중에 앱을 종료하는 경우, 로그인 후 다시 signup 화면으로 도달
 * 3. 정상적으로 signup 까지 완료한 경우, 메인화면으로 이동
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val checkVersionUpdateUseCase: CheckVersionUpdateUseCase,
    private val setMemberIdUseCase: SetMemberIdUseCase,
    private val getCurrentMemberInfoUseCase: GetCurrentMemberInfoUseCase,
    private val checkAdminPasswordUseCase: CheckAdminPasswordUseCase,
    private val shouldShowGuestButtonUseCase: ShouldShowGuestButtonUseCase,
    private val kakaoSdkProvider: KakaoSdkProviderInterface,
    private val resourceProvider: ResourceProvider,
) : BaseViewModel<LoginUiState, LoginUiSideEffect, LoginUiEvent>(LoginUiState()) {

    init {
        viewModelScope.launch {
            checkRequireVersionUpdate()
            shouldShowGuestButtonUseCase()
                .onSuccess { setState { copy(isGuestButtonVisible = it) } }
                .onFailure { FirebaseCrashlytics.getInstance().recordException(it) }
        }
    }

    private suspend fun checkRequireVersionUpdate() {
        checkVersionUpdateUseCase(resourceProvider.getVersionCode())
            .onSuccess { versionType ->
                when (versionType) {
                    VersionType.NOT_REQUIRED -> Unit
                    VersionType.REQUIRED -> setState { copy(dialogState = REQUIRE_UPDATE) }
                    VersionType.UPDATED_BUT_NOT_REQUIRED -> setState { copy(dialogState = NECESSARY_UPDATE) }
                }
            }
            .onFailure {
                setState { copy(dialogState = FAIL_INIT_LOAD) }
            }
    }

    private fun kakaoLogin() {
        kakaoSdkProvider.login(
            onSuccess = {
                validateKakaoToken()
                setState { copy(isLoading = false) }
            },
            onFailed = {
                setEffect(LoginUiSideEffect.ShowToast("로그인 실패"))
                setState { copy(isLoading = false) }
            }
        )
    }

    private fun validateKakaoToken() {
        kakaoSdkProvider.validateAccessToken(
            onSuccess = { userAccountId ->
                viewModelScope.launch {
                    setMemberId(userAccountId)
                    validateRegisteredUser()
                }
            },
            onFailed = {
                setEffect(LoginUiSideEffect.ShowToast("로그인 실패"))
                setState { copy(isLoading = false) }
            }
        )
    }

    private suspend fun validateRegisteredUser() {
        getCurrentMemberInfoUseCase()
            .onSuccess { currentMemberInfo ->
                if (currentMemberInfo == null) {
                    // 존재하지 않는다면, signup 화면으로 이동한다.
                    setEffect(LoginUiSideEffect.NavigateToSignUpScreen)
                    return@onSuccess
                }

                // 이미 firebase 에 존재하는 유저인 경우 바로 메인화면으로 이동한다. (일반적)
                setEffect(LoginUiSideEffect.NavigateToQRMainScreen)
            }
            .onFailure {
                setEffect(LoginUiSideEffect.ShowToast("로그인 실패"))
                setState { copy(isLoading = false) }
            }
    }

    private suspend fun setMemberId(id: Long) {
        // user id 를 setting 할 때까지 coroutine scope 가 유지되어야 하므로,
        // withContext 로 선언
        withContext(viewModelScope.coroutineContext) {
            setMemberIdUseCase(id)
        }
    }

    fun adminLogin(password: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            checkAdminPasswordUseCase(CheckAdminPasswordUseCase.Params(password))
                .onSuccess { isCorrect ->
                    if (isCorrect) {
                        setEffect(LoginUiSideEffect.NavigateToAdminScreen)
                    } else {
                        setEffect(LoginUiSideEffect.ShowToast("로그인 실패"))
                    }
                    setState { copy(isLoading = false) }
                }
                .onFailure { exception ->
                    setEffect(LoginUiSideEffect.ShowToast("${exception.message}"))
                    setState { copy(isLoading = false) }
                }
        }
    }

    override suspend fun handleEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnLoginButtonClicked -> {
                setState { copy(isLoading = true) }
                kakaoLogin()
            }

            is LoginUiEvent.OnSkipButtonClicked -> {
                setEffect(
                    LoginUiSideEffect.NavigateToQRMainScreen
                )
            }

            is LoginUiEvent.OnYappuImageClicked -> {
                setState {
                    if (this.clickCount == HIDDEN_CONDITION) {
                        this.copy(clickCount = 0, dialogState = NONE)
                    } else {
                        this.copy(clickCount = this.clickCount + 1)
                    }
                }
            }

            is LoginUiEvent.OnUpdateButtonClicked -> {
                setEffect(
                    LoginUiSideEffect.NavigateToPlayStore
                )

                // 강제 업데이트가 필요하지 않은 경우 '업데이트' 버튼 클릭 시 다이얼로그를 닫는다.
                if (currentState.dialogState == NECESSARY_UPDATE) {
                    setState { copy(dialogState = NONE) }
                }
            }

            is LoginUiEvent.OnCancelButtonClicked -> {
                setState { copy(dialogState = NONE) }
            }

            is LoginUiEvent.OnGuestButtonClicked -> {
                //게스트 로그인의 경우 랜덤으로 memberId 삽입
                setMemberId(Random.nextLong())
                setEffect(
                    LoginUiSideEffect.NavigateToSignUpScreen
                )
            }

            is LoginUiEvent.OnExitButtonClicked -> {
                setEffect(
                    LoginUiSideEffect.ExitProcess
                )
            }
        }
    }

    companion object {
        const val HIDDEN_CONDITION = 15
    }
}
