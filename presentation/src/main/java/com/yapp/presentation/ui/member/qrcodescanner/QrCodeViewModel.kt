package com.yapp.presentation.ui.member.qrcodescanner

import com.yapp.common.base.BaseViewModel
import com.yapp.common.util.AttendanceQrCodeParser
import com.yapp.presentation.ui.member.qrcodescanner.QrCodeContract.*
import javax.inject.Inject

class QrCodeViewModel @Inject constructor(
) : BaseViewModel<QrCodeUiState, QrCodeUiSideEffect, QrCodeUiEvent>(
    QrCodeUiState()
) {
    init {
        // todo: DB 확인 후 오늘의 세션 출석이 완료되었는지 확인 후 attendanceState 변경
    }

    override suspend fun handleEvent(event: QrCodeUiEvent) {
        when (event) {
            is QrCodeUiEvent.ScanQrCode -> {
                val sessionId = parseQrCode(event.codeValue)
                if (sessionId != -1) {
                    markAttendance(sessionId)
                } else {
                    setEffect(QrCodeUiSideEffect.ShowToast("잘못된 코드입니다."))
                }
            }
        }
    }

    private fun parseQrCode(codeValue: String?): Int {
        return try {
            AttendanceQrCodeParser.getSessionIdFromBarcode(codeValue)
        } catch (e: Exception) {
            return -1
        }
    }

    private fun markAttendance(sessionId: Int) {
        // todo: DB에 출석 결과 반영
        setState {
            copy(
                attendanceState = AttendanceState.SUCCESS
            )
        }
    }
}