package com.yapp.presentation.ui.member.main

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class QRMainContract {
    data class QRMainUiState(
        val isLoading: Boolean = true,
        val showDialog: Boolean = false,
        val selectedButtonId: Int = 0,
//todo 변경 필요
        val time: String = "",
        val isAttendanceCompleted: Boolean = false,
    ) : UiState

    sealed class QRMainUiSideEffect : UiSideEffect {
        data class ShowToast(val msg: String) : QRMainUiSideEffect()
    }

    sealed class QRMainUiEvent : UiEvent {
        object OnSnackBarButtonClicked : QRMainUiEvent()
        object OnDialogButtonClicked : QRMainUiEvent()
        object CloseDialog: QRMainUiEvent()
        data class OnClickSelectableButtonClicked(val num: Int): QRMainUiEvent()
    }
}
