package com.yapp.presentation.ui.member.qrcodescanner

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.consume
import javax.inject.Inject

class QrCodeViewModel @Inject constructor(
    val qrStateProvider: QrStateProvider
) :ViewModel() {

    init {
        qrStateProvider.qrState.consume {

        }
    }
}