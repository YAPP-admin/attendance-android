package com.yapp.presentation.ui.member.signup.password

import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.CheckSignUpPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PassWordViewModel @Inject constructor(
    private val checkSignUpPasswordUseCase: CheckSignUpPasswordUseCase,
) :
    BaseViewModel<PasswordContract.PasswordUiState, PasswordContract.PasswordSideEffect, PasswordContract.PasswordUiEvent>(
        PasswordContract.PasswordUiState()
    ) {

    override suspend fun handleEvent(event: PasswordContract.PasswordUiEvent) {
        when (event) {
            is PasswordContract.PasswordUiEvent.InputPassword -> {
                updatePassword(password = event.password)
            }

            PasswordContract.PasswordUiEvent.OnBackButtonClick -> {
                setEffect(PasswordContract.PasswordSideEffect.NavigateToPreviousScreen)
            }

            PasswordContract.PasswordUiEvent.OnNextButtonClick -> {
                checkPassword(password = currentState.inputPassword)
            }
        }
    }

    private fun updatePassword(password: String) {
        if (password.length <= PasswordDigit) {
            setState { copy(inputPassword = password, isWrong = false) }
        }
    }

    private fun checkPassword(password: String) = viewModelScope.launch {
        setEffect(PasswordContract.PasswordSideEffect.KeyboardHide)
        checkSignUpPasswordUseCase(password)
            .onSuccess { isPasswordValid ->
                when (isPasswordValid) {
                    true -> {
                        setEffect(PasswordContract.PasswordSideEffect.NavigateToNextScreen)
                    }

                    false -> {
                        setEffect(PasswordContract.PasswordSideEffect.KeyboardHide)
                        setState {
                            copy(
                                isWrong = true,
                            )
                        }
                    }
                }
            }.onFailure { exception ->
                if (exception is FirebaseNetworkException) {
                    setEffect(PasswordContract.PasswordSideEffect.ShowToast("네트워크 연결을 확인해주세요"))
                }
            }
    }
}
