package com.yapp.presentation.ui.member.qrcodescanner

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class QrCodeContract {
    data class QrCodeUiState(
        val isLoading: Boolean = false,
        val attendanceState: AttendanceState = AttendanceState.STAND_BY
    ) : UiState

    sealed class QrCodeUiSideEffect : UiSideEffect {
        class ShowToast(val msg: String) : QrCodeUiSideEffect()
    }

    sealed class QrCodeUiEvent : UiEvent {
        class ScanQrCode(val codeValue: String?) : QrCodeUiEvent()
        class ScanWrongCode(val informText: String) : QrCodeUiEvent()
    }

    enum class AttendanceState {
        STAND_BY,
        SUCCESS
    }
}