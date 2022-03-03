package com.yapp.presentation.ui.member.signup

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class NameContract {
    data class NameUiState(
        val isLoading: Boolean = false
    ) : UiState

    sealed class NameSideEffect : UiSideEffect {}
    sealed class NameUiEvent : UiEvent {}
}