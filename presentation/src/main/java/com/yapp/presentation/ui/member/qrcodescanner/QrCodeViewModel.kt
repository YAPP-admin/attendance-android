package com.yapp.presentation.ui.member.qrcodescanner

import com.yapp.common.base.BaseViewModel
import com.yapp.common.util.AttendanceQrCodeParser
import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.model.AttendanceTypeEntity
import com.yapp.domain.usecases.GetMemberIdUseCase
import com.yapp.domain.usecases.SetMemberAttendanceUseCase
import com.yapp.presentation.model.collections.AttendanceList
import com.yapp.presentation.ui.member.qrcodescanner.QrCodeContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QrCodeViewModel @Inject constructor(
    private val getMemberIdUseCase: GetMemberIdUseCase,
    private val setMemberAttendanceUseCase: SetMemberAttendanceUseCase
) : BaseViewModel<QrCodeUiState, QrCodeUiSideEffect, QrCodeUiEvent>(
    QrCodeUiState()
) {
    var upcomingSessionId = AttendanceList.DEFAULT_UPCOMING_SESSION_ID

    init {
        // TODO: Upcoming Session Id 받아오기
        upcomingSessionId = 3
    }

    override suspend fun handleEvent(event: QrCodeUiEvent) {
        when (event) {
            is QrCodeUiEvent.ScanQrCode -> {
                when (val sessionId = parseQrCode(event.codeValue)) {
                    AttendanceList.DEFAULT_UPCOMING_SESSION_ID -> setEffect(
                        QrCodeUiSideEffect.ShowToast("오늘의 세션 정보를 불러오지 못했습니다.")
                    )
                    upcomingSessionId -> {
                        markAttendance(sessionId)
                    }
                    else -> QrCodeUiSideEffect.ShowToast("잘못된 코드입니다.")
                }
            }
            is QrCodeUiEvent.ScanWrongCode -> setEffect(
                QrCodeUiSideEffect.ShowToast(event.informText)
            )
        }
    }

    private fun parseQrCode(codeValue: String?): Int {
        return try {
            AttendanceQrCodeParser.getSessionIdFromBarcode(codeValue)
        } catch (e: Exception) {
            return AttendanceList.DEFAULT_UPCOMING_SESSION_ID
        }
    }

    private suspend fun markAttendance(sessionId: Int) {
        val userId = getUserId()
        setMemberAttendanceUseCase(
            params = SetMemberAttendanceUseCase.Params(
                memberId = userId,
                sessionId = sessionId,
                changedAttendance = AttendanceEntity(
                    sessionId = sessionId,
                    type = AttendanceTypeEntity.Normal
                )
            )
        ).collectWithCallback(
            onSuccess = { setState { copy(attendanceState = AttendanceState.SUCCESS) } },
            onFailed = { QrCodeUiSideEffect.ShowToast("출석에 실패했습니다") }
        )

    }

    private suspend fun getUserId(): Long {
        var userId = -1L
        getMemberIdUseCase().collectWithCallback(
            onSuccess = {
                userId = it ?: -1L
            },
            onFailed = {
                QrCodeUiSideEffect.ShowToast("ID를 받아오지 못했습니다.")
            }
        )
        return userId
    }
}