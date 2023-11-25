package com.yapp.presentation.ui.admin.createsession

import com.yapp.common.base.BaseViewModel
import com.yapp.presentation.ui.admin.createsession.CreateSessionContract.CreateSessionUiEvent
import com.yapp.presentation.ui.admin.createsession.CreateSessionContract.CreateSessionUiSideEffect
import com.yapp.presentation.ui.admin.createsession.CreateSessionContract.CreateSessionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

@HiltViewModel
class CreateSessionViewModel @Inject constructor(
) : BaseViewModel<CreateSessionUiState, CreateSessionUiSideEffect, CreateSessionUiEvent>(
    CreateSessionUiState()
) {
    init {
        setState {
            copy(
                code = Random.nextInt(RANDOM_CODE_RANGE).toString()
            )
        }
    }

    override suspend fun handleEvent(event: CreateSessionUiEvent) {
        // TODO
    }

    companion object {
        private val RANDOM_CODE_RANGE = 1000..9999
    }
}
