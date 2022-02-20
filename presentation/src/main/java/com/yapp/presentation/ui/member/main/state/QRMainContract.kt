package com.yapp.presentation.ui.member.main.state

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class QRMainContract {
    data class QRMainUiState(
        val isLoading: Boolean = true,
//todo 변경 필요
        val time: String = "",
        val isAttendanceCompleted: Boolean = false,
    ) : UiState

    sealed class QRMainUiSideEffect : UiSideEffect {
        object ShowToast : QRMainUiSideEffect()
    }

    sealed class QRMainUiEvent : UiEvent {
        object onButtonClicked : QRMainUiEvent()
    }
}
