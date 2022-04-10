package com.yapp.presentation.ui.member.signup.name

import com.yapp.common.base.BaseViewModel
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
        }
    }

}