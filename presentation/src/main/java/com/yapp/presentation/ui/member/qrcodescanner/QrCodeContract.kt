package com.yapp.presentation.ui.member.qrcodescanner

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class QrCodeContract {
    data class QrCodeUiState(
        val code: String = ""
    ) : UiState

    sealed class QrCodeUiSideEffect : UiSideEffect {
    }

    sealed class QrCodeUiEvent : UiEvent {
    }
}