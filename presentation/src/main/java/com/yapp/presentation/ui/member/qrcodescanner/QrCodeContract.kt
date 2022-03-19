package com.yapp.presentation.ui.member.qrcodescanner

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class QrCodeContract {
    data class QrCodeUiState(
        val attendanceState: AttendanceState = AttendanceState.STAND_BY,
        val sessionId: Int = -1
    ) : UiState

    sealed class QrCodeUiSideEffect : UiSideEffect {}

    sealed class QrCodeUiEvent : UiEvent {
        class SuccessToCheck(val sessionId: Int): QrCodeUiEvent()
    }

    enum class AttendanceState {
        STAND_BY,
        SUCCESS,
        COMPLETE
    }
}