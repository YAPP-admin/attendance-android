package com.yapp.presentation.ui.member.qrcodescanner

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.common.util.AttendanceQrCodeParser
import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.model.AttendanceTypeEntity
import com.yapp.domain.usecases.GetMaginotlineTimeUseCase
import com.yapp.domain.usecases.GetMemberIdUseCase
import com.yapp.domain.usecases.SetMemberAttendanceUseCase
import com.yapp.presentation.R
import com.yapp.presentation.common.AttendanceBundle
import com.yapp.presentation.model.collections.AttendanceList
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
    private val getMemberIdUseCase: GetMemberIdUseCase,
    private val setMemberAttendanceUseCase: SetMemberAttendanceUseCase
) : BaseViewModel<QrCodeUiState, QrCodeUiSideEffect, QrCodeUiEvent>(
    QrCodeUiState()
) {
    var isAvailableToScan = true
    var todaySessionId = AttendanceList.DEFAULT_UPCOMING_SESSION_ID
    var userId = 0L

    init {
        if (AttendanceBundle.upComingSessionId == AttendanceList.DEFAULT_UPCOMING_SESSION_ID) {
            guideMoveBackToHomeAndDeactivateScan()
        } else {
            todaySessionId = AttendanceBundle.upComingSessionId
        }

        viewModelScope.launch {
            getMaginotlineTime()
            getUserId()
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

    private suspend fun getUserId() {
        getMemberIdUseCase().collectWithCallback(
            onSuccess = { id ->
                if (id != null) userId = id
                else guideMoveBackToHomeAndDeactivateScan()
            },
            onFailed = { guideMoveBackToHomeAndDeactivateScan() }
        )
    }

    private suspend fun parseQrCode(codeValue: String?) {
        isAvailableToScan = false
        try {
            val paredSessionId = AttendanceQrCodeParser.getSessionIdFromBarcode(codeValue)
            if (paredSessionId == todaySessionId) markAttendance(paredSessionId)
            else notifyErrorMessageAndActivateScan(resourceProvider.getString(R.string.member_qr_scan_correct_code_error_message))
        } catch (e: Exception) {
            notifyErrorMessageAndActivateScan(resourceProvider.getString(R.string.member_qr_scan_correct_code_error_message))
        }
    }

    private suspend fun markAttendance(targetSessionId: Int) {
        setMemberAttendanceUseCase(
            params = SetMemberAttendanceUseCase.Params(
                memberId = userId,
                sessionId = targetSessionId,
                changedAttendance = AttendanceEntity(
                    sessionId = targetSessionId,
                    type = AttendanceTypeEntity.Normal
                )
            )
        ).collectWithCallback(
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