package com.yapp.presentation.ui.admin.main

import com.yapp.common.base.BaseViewModel
import com.yapp.presentation.ui.admin.main.AdminMainContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminMainViewModel @Inject constructor(
) : BaseViewModel<AdminMainUiState, AdminMainUiSideEffect, AdminMainUiEvent>(
    AdminMainUiState()
) {

    override fun setEvent(event: AdminMainUiEvent) {
        super.setEvent(event)
    }

    override fun handleEvent(event: AdminMainUiEvent) {
        TODO("Not yet implemented")
    }
}
