package com.yapp.presentation.ui.member.qrcodescanner

import android.util.Log
import com.yapp.common.base.BaseViewModel
import com.yapp.presentation.ui.member.qrcodescanner.QrCodeContract.*
import javax.inject.Inject

class QrCodeViewModel @Inject constructor(
) : BaseViewModel<QrCodeUiState, QrCodeUiSideEffect, QrCodeUiEvent>(
    QrCodeUiState()
) {
    init {
        // todo: DB 확인 후 오늘의 세션 출석이 완료되었는지 확인 후 checkState 변경
    }

    override fun handleEvent(event: QrCodeUiEvent) {
        when (event) {
            is QrCodeUiEvent.QRCodeScanned -> {
                setState {
                    copy(
                        checkState = CheckState.SUCCESS,
                        sessionId = event.sessionId
                    )
                }
            }
        }
    }
}