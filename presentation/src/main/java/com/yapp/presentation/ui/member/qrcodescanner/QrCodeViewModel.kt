package com.yapp.presentation.ui.member.qrcodescanner

import com.yapp.common.base.BaseViewModel
import javax.inject.Inject

class QrCodeViewModel @Inject constructor(
) : BaseViewModel<QrCodeContract.QrCodeUiState, QrCodeContract.QrCodeUiSideEffect, QrCodeContract.QrCodeUiEvent>(
    QrCodeContract.QrCodeUiState()
) {
    override fun handleEvent(event: QrCodeContract.QrCodeUiEvent) {

    }
}