package com.yapp.presentation.ui.member.signup.name

import com.yapp.common.base.BaseViewModel
import com.yapp.presentation.ui.member.signup.name.NameContract.NameSideEffect.NavigateToNextScreen
import com.yapp.presentation.ui.member.signup.name.NameContract.NameSideEffect.NavigateToPreviousScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NameViewModel @Inject constructor() :
    BaseViewModel<NameContract.NameUiState, NameContract.NameSideEffect, NameContract.NameUiEvent>(
        NameContract.NameUiState()
    ) {
    override suspend fun handleEvent(event: NameContract.NameUiEvent) {
        when (event) {
            is NameContract.NameUiEvent.InputName -> {
                setState { copy(name = event.name) }
            }
            is NameContract.NameUiEvent.OnCancelButtonClick -> {
                setEffect(NavigateToPreviousScreen)
            }
            is NameContract.NameUiEvent.OnNextButtonClick -> {
                setEffect(NavigateToNextScreen(uiState.value.name))
            }
            is NameContract.NameUiEvent.OnBackButtonClick -> {
                setState { copy(showDialog = true) }
            }
            is NameContract.NameUiEvent.OnDismissDialogButtonClick -> {
                setState { copy(showDialog = false) }
            }
        }
    }
}
