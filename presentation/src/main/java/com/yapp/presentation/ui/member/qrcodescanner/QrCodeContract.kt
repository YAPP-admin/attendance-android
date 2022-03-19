package com.yapp.presentation.ui.member.qrcodescanner

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class QrCodeContract {
    data class QrCodeUiState(
        val checkState: CheckState = CheckState.BEFORE,
        val sessionId: Int = -1
    ) : UiState

    sealed class QrCodeUiSideEffect : UiSideEffect {}

    sealed class QrCodeUiEvent : UiEvent {
        class QRCodeScanned(val sessionId: Int): QrCodeUiEvent()
    }
}