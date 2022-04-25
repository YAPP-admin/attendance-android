package com.yapp.presentation.ui.member.qrcodescanner

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class QrCodeContract {
    data class QrCodeUiState(
        val isLoading: Boolean = false,
        val attendanceState: AttendanceState = AttendanceState.STAND_BY,
        val maginotlineTime: String = "",
        val toastMessage: String = ""
    ) : UiState {
        enum class LoadState {
            Loading, Idle, Error
        }
    }

    sealed class QrCodeUiSideEffect : UiSideEffect {
        object ShowToast : QrCodeUiSideEffect()
        object ShowToastAndHide : QrCodeUiSideEffect()
    }

    sealed class QrCodeUiEvent : UiEvent {
        data class ScanQrCode(val codeValue: String?) : QrCodeUiEvent()
        data class GetScannerError(val errorMessage: String) : QrCodeUiEvent()
    }

    enum class AttendanceState {
        STAND_BY,
        SUCCESS
    }
}