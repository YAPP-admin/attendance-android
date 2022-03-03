package com.yapp.presentation.ui.member.signup

import com.yapp.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NameViewModel @Inject constructor() :
    BaseViewModel<NameContract.NameUiState, NameContract.NameSideEffect, NameContract.NameUiEvent>(
        NameContract.NameUiState()
    ) {
    override fun handleEvent(event: NameContract.NameUiEvent) {
        TODO("Not yet implemented")
    }

}