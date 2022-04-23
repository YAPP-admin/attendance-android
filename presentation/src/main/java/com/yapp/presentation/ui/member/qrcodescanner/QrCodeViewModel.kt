package com.yapp.presentation.ui.member.qrcodescanner

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.common.util.AttendanceQrCodeParser
import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.model.AttendanceTypeEntity
import com.yapp.domain.usecases.GetMaginotlineTimeUseCase
import com.yapp.domain.usecases.GetMemberIdUseCase
import com.yapp.domain.usecases.SetMemberAttendanceUseCase
import com.yapp.presentation.model.collections.AttendanceList
import com.yapp.presentation.ui.member.qrcodescanner.QrCodeContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QrCodeViewModel @Inject constructor(
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
        todaySessionId = TodaySessionInfo.todaySessionId

        viewModelScope.launch {
            getMaginotlineTime()
            getUserId()
        }
    }

    override suspend fun handleEvent(event: QrCodeUiEvent) {
        when (event) {
            is QrCodeUiEvent.ScanQrCode -> {
                if (isAvailableToScan) afterScannedCode(event.codeValue)
            }
            is QrCodeUiEvent.GetErrorMessage -> {
                setState { copy(toastMsg = event.errorMessage) }
                setEffect(QrCodeUiSideEffect.ShowToast)
            }
        }
    }

    private suspend fun afterScannedCode(codeValue: String?) {
        isAvailableToScan = false

        try {
            val paredSessionId = AttendanceQrCodeParser.getSessionIdFromBarcode(codeValue)
            if (paredSessionId == todaySessionId) markAttendance(paredSessionId)
            else handleQRError("잘못된 코드입니다")
        } catch (e: Exception) {
            handleQRError("잘못된 형식의 코드입니다")
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
            onFailed = { handleQRError("출석 정보 반영에 실패했습니다") }
        )
    }

    private suspend fun getUserId() {
        getMemberIdUseCase().collectWithCallback(
            onSuccess = { id ->
                if (id != null) userId = id
                else handleQRError("ID를 받아오지 못했습니다")
            },
            onFailed = { handleQRError("ID를 받아오지 못했습니다") }
        )
    }

    private suspend fun getMaginotlineTime() {
        setState { copy(isLoading = true) }
        getMaginotlineTimeUseCase().collectWithCallback(
            onSuccess = { maginotline -> setState { copy(maginotlineTime = maginotline) } },
            onFailed = {
                // TODO: 출석 마감 정보 불러오지 못했을 때의 에러 처리
            }
        )
        setState { copy(isLoading = false) }
    }

    private suspend fun handleQRError(errorMsg: String) {
        setState { copy(toastMsg = errorMsg) }
        setEffect(QrCodeUiSideEffect.ShowToast)
        delay(1500L)
        isAvailableToScan = true
    }
}