package com.yapp.presentation.ui.member.qrcodescanner

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class QrCodeContract {
    data class QrCodeUiState(
        val isLoading: Boolean = false,
        val attendanceState: AttendanceState = AttendanceState.STAND_BY,
        val maginotlineTime: String = "",
        val toastMsg: String = ""
    ) : UiState {
        enum class LoadState {
            Loading, Idle, Error
        }
    }

    sealed class QrCodeUiSideEffect : UiSideEffect {
        object ShowToast : QrCodeUiSideEffect()
    }

    sealed class QrCodeUiEvent : UiEvent {
        class ScanQrCode(val codeValue: String?) : QrCodeUiEvent()
        class GetErrorMessage(val errorMessage: String) : QrCodeUiEvent()
    }

    enum class AttendanceState {
        STAND_BY,
        SUCCESS
    }
}