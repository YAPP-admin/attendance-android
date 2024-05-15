package com.yapp.presentation.ui.member.signup.password

import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.CheckSessionPasswordUseCase
import com.yapp.domain.usecases.CheckSignUpPasswordUseCase
import com.yapp.presentation.common.AttendanceBundle
import com.yapp.presentation.ui.member.signup.password.PasswordContract.PasswordSideEffect
import com.yapp.presentation.ui.member.signup.password.PasswordContract.PasswordUiEvent
import com.yapp.presentation.ui.member.signup.password.PasswordContract.PasswordUiState
import com.yapp.presentation.ui.member.signup.password.PasswordContract.PasswordUiState.Companion.PasswordDigit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PassWordViewModel @Inject constructor(
    private val checkSignUpPasswordUseCase: CheckSignUpPasswordUseCase,
    private val checkSessionPasswordUseCase: CheckSessionPasswordUseCase
) : BaseViewModel<PasswordUiState, PasswordSideEffect, PasswordUiEvent>(PasswordUiState()) {

    override suspend fun handleEvent(event: PasswordUiEvent) {
        when (event) {
            is PasswordUiEvent.InputPassword -> {
                updatePassword(password = event.password)
            }

            PasswordUiEvent.OnBackButtonClick -> {
                setEffect(PasswordSideEffect.NavigateToPreviousScreen)
            }

            is PasswordUiEvent.OnNextButtonClick -> {
                when (event.type) {
                    PasswordType.SignUp -> checkSignUpPassword(currentState.inputPassword)
                    PasswordType.Session -> checkSessionPassword(currentState.inputPassword)
                }
            }
        }
    }

    private fun updatePassword(password: String) {
        if (password.length <= PasswordDigit) {
            setState { copy(inputPassword = password, isWrong = false) }
        }
    }

    private fun checkSignUpPassword(password: String) = viewModelScope.launch {
        setEffect(PasswordSideEffect.KeyboardHide)
        checkSignUpPasswordUseCase(password)
            .onSuccess { isPasswordValid ->
                when (isPasswordValid) {
                    true -> {
                        setEffect(PasswordSideEffect.NavigateToNextScreen)
                    }

                    false -> {
                        setState {
                            copy(
                                isWrong = true,
                            )
                        }
                    }
                }
            }.onFailure { exception ->
                if (exception is FirebaseNetworkException) {
                    setEffect(PasswordSideEffect.ShowToast("네트워크 연결을 확인해주세요"))
                }
            }
    }

    private fun checkSessionPassword(password: String) = viewModelScope.launch {
        setEffect(PasswordSideEffect.KeyboardHide)

        // TODO AttendanceBundle을 사용하지 않고 Navagation의 Argument로 넘어온 Id를 사용하도록 수정할것
        val sessionId = AttendanceBundle.upComingSession!!.sessionId

        checkSessionPasswordUseCase(sessionId = sessionId, inputPassword = password)
            .onSuccess { isPasswordValid ->
                when (isPasswordValid) {
                    true -> {
                        setEffect(PasswordSideEffect.NavigateToNextScreen)
                    }

                    false -> {
                        setState {
                            copy(
                                isWrong = true,
                            )
                        }
                    }
                }
            }.onFailure { exception ->
                if (exception is FirebaseNetworkException) {
                    setEffect(PasswordSideEffect.ShowToast("네트워크 연결을 확인해주세요"))
                }
            }
    }

}
