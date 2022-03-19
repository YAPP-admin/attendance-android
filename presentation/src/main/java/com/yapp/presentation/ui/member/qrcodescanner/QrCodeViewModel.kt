package com.yapp.presentation.ui.member.qrcodescanner

import com.yapp.common.base.BaseViewModel
import com.yapp.presentation.ui.member.qrcodescanner.QrCodeContract.*
import javax.inject.Inject

class QrCodeViewModel @Inject constructor(
) : BaseViewModel<QrCodeUiState, QrCodeUiSideEffect, QrCodeUiEvent>(
    QrCodeUiState()
) {
    init {
        // todo: DB 확인 후 오늘의 세션 출석이 완료되었는지 확인 후 attendanceState 변경
    }

    override fun handleEvent(event: QrCodeUiEvent) {
        when (event) {
            is QrCodeUiEvent.SuccessToCheck -> {
                setState {
                    copy(
                        attendanceState = AttendanceState.SUCCESS,
                        sessionId = event.sessionId
                    )
                }
            }
        }
    }
}