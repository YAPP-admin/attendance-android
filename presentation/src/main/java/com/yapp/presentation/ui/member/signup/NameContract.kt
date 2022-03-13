package com.yapp.presentation.ui.member.signup

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class NameContract {
    data class NameUiState(
        val isLoading: Boolean = false,
        val name: String = ""
    ) : UiState

    sealed class NameSideEffect : UiSideEffect {}
    sealed class NameUiEvent : UiEvent {
        data class InputName(val name:String) : NameUiEvent()
    }
}