package com.yapp.presentation.ui.member.qrcodescanner

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class QrCodeContract {
    data class QrCodeUiState(
        val isGrantedCameraPermission: Boolean = false
    ) : UiState

    sealed class QrCodeUiSideEffect : UiSideEffect {
    }

    sealed class QrCodeUiEvent : UiEvent {
    }
}