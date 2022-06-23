package com.yapp.presentation.ui.member.qrcodescanner

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.presentation.common.AttendanceQrCodeParser
import com.yapp.domain.usecases.GetMaginotlineTimeUseCase
import com.yapp.domain.usecases.CheckQrPasswordUseCase
import com.yapp.domain.usecases.MarkAttendanceUseCase
import com.yapp.presentation.R
import com.yapp.presentation.common.AttendanceBundle
import com.yapp.presentation.model.QrInformation
import com.yapp.presentation.model.Session
import com.yapp.presentation.ui.member.qrcodescanner.QrCodeContract.*
import com.yapp.presentation.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QrCodeViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val getMaginotlineTimeUseCase: GetMaginotlineTimeUseCase,
    private val markAttendanceUseCase: MarkAttendanceUseCase,
    private val checkQrPasswordUseCase: CheckQrPasswordUseCase,
) : BaseViewModel<QrCodeUiState, QrCodeUiSideEffect, QrCodeUiEvent>(
    QrCodeUiState()
) {
    var isAvailableToScan = true
    lateinit var todaySession: Session

    init {
        AttendanceBundle.upComingSession?.let { todaySession = it }
            ?: guideMoveBackToHomeAndDeactivateScan()

        viewModelScope.launch {
            getMaginotlineTime()
        }
    }

    override suspend fun handleEvent(event: QrCodeUiEvent) {
        when (event) {
            is QrCodeUiEvent.ScanQrCode -> {
                if (isAvailableToScan) parseQrCode(event.codeValue)
            }
            is QrCodeUiEvent.GetScannerError -> {
                notifyErrorMessageAndActivateScan(resourceProvider.getString(R.string.member_qr_get_error_please_retry_message))
            }
        }
    }

    private suspend fun getMaginotlineTime() {
        setState { copy(isLoading = true) }
        getMaginotlineTimeUseCase().collectWithCallback(
            onSuccess = { maginotline -> setState { copy(maginotlineTime = maginotline) } },
            onFailed = { guideMoveBackToHomeAndDeactivateScan() }
        )
        setState { copy(isLoading = false) }
    }

    private suspend fun parseQrCode(codeValue: String?) {
        isAvailableToScan = false
        try {
            val qrInformation = AttendanceQrCodeParser.getSessionInformationFromQrcode(codeValue)
            if (qrInformation.sessionId == todaySession.sessionId) checkQrPassword(qrInformation)
            else notifyErrorMessageAndActivateScan(resourceProvider.getString(R.string.member_qr_scan_correct_code_error_message))
        } catch (e: Exception) {
            notifyErrorMessageAndActivateScan(resourceProvider.getString(R.string.member_qr_scan_correct_code_error_message))
        }
    }

    private suspend fun checkQrPassword(qrInformation: QrInformation) {
        checkQrPasswordUseCase(qrInformation.password).collectWithCallback(
            onSuccess = { isCorrectPassword ->
                if (isCorrectPassword) markAttendance()
                else notifyErrorMessageAndActivateScan(resourceProvider.getString(R.string.member_qr_scan_correct_code_error_message))
            },
            onFailed = { notifyErrorMessageAndActivateScan(resourceProvider.getString(R.string.member_qr_get_error_please_retry_message)) }
        )
    }

    private suspend fun markAttendance() {
        markAttendanceUseCase(todaySession.toEntity()).collectWithCallback(
            onSuccess = { setState { copy(attendanceState = AttendanceState.SUCCESS) } },
            onFailed = { notifyErrorMessageAndActivateScan(resourceProvider.getString(R.string.member_qr_get_error_please_retry_message)) }
        )
    }

    private suspend fun notifyErrorMessageAndActivateScan(errorMsg: String) {
        setState { copy(toastMessage = errorMsg) }
        setEffect(QrCodeUiSideEffect.ShowToastAndHide)
        delay(1500L)
        isAvailableToScan = true
    }

    private fun guideMoveBackToHomeAndDeactivateScan() {
        isAvailableToScan = false
        setState { copy(toastMessage = resourceProvider.getString(R.string.member_qr_move_back_to_home_and_retry_error_message)) }
        setEffect(QrCodeUiSideEffect.ShowToast)
    }
}