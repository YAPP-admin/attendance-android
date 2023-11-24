package com.yapp.presentation.ui.admin.createsession

import com.yapp.common.base.BaseViewModel
import com.yapp.presentation.ui.admin.createsession.CreateSessionContract.CreateSessionUiEvent
import com.yapp.presentation.ui.admin.createsession.CreateSessionContract.CreateSessionUiSideEffect
import com.yapp.presentation.ui.admin.createsession.CreateSessionContract.CreateSessionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateSessionViewModel @Inject constructor(
) : BaseViewModel<CreateSessionUiState, CreateSessionUiSideEffect, CreateSessionUiEvent>(
    CreateSessionUiState()
) {

    override suspend fun handleEvent(event: CreateSessionUiEvent) {
        // TODO
    }
}
