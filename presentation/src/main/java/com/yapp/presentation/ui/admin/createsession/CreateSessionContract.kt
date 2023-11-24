package com.yapp.presentation.ui.admin.createsession

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class CreateSessionContract {
    data class CreateSessionUiState(
        val loadState: LoadState = LoadState.Idle,
    ) : UiState {
        enum class LoadState {
            Loading, Idle
        }
    }

    sealed class CreateSessionUiSideEffect : UiSideEffect
    sealed class CreateSessionUiEvent : UiEvent
}
