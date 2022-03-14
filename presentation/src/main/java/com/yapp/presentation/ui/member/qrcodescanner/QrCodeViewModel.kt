package com.yapp.presentation.ui.member.qrcodescanner

import com.yapp.common.base.BaseViewModel
import com.yapp.presentation.ui.member.qrcodescanner.QrCodeContract.*
import javax.inject.Inject

class QrCodeViewModel @Inject constructor(
) : BaseViewModel<QrCodeUiState, QrCodeUiSideEffect, QrCodeUiEvent>(
    QrCodeUiState()
) {
    override fun handleEvent(event: QrCodeUiEvent) {
        when (event) {

        }
    }
}