package com.yapp.presentation.ui

import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.CheckQrAuthTimeUseCase
import com.yapp.domain.usecases.MarkAttendanceUseCase
import com.yapp.presentation.R
import com.yapp.presentation.common.AttendanceBundle
import com.yapp.presentation.ui.MainContract.MainUiEvent
import com.yapp.presentation.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val resourcesProvider: ResourceProvider,
    private val checkQrAuthTime: CheckQrAuthTimeUseCase,
    private val markAttendanceUseCase: MarkAttendanceUseCase
) : BaseViewModel<MainContract.MainUiState, MainContract.MainUiSideEffect, MainUiEvent>(
    MainContract.MainUiState()
) {

    override suspend fun handleEvent(event: MainUiEvent) {
        when (event) {
            MainUiEvent.OnClickQrAuthButton -> checkAttendanceValidate()
            MainUiEvent.OnValidatePassword -> onMarkAttendance()
        }
    }

    private suspend fun checkAttendanceValidate() = coroutineScope {
        if (AttendanceBundle.isAbsent) {
            checkQrAuthTime()
                .onSuccess { isQRCheckEnable ->
                    if (isQRCheckEnable) {
                        setEffect(MainContract.MainUiSideEffect.NavigateToPassword)
                    } else {
                        showToast(resourcesProvider.getString(R.string.member_main_qr_enter_failed_toast_message))
                    }
                }
                .onFailure {
                    showToast(resourcesProvider.getString(R.string.member_main_qr_enter_failed_toast_message))
                }
        } else {
            showToast(resourcesProvider.getString(R.string.member_main_qr_enter_completed_toast_message))
        }
    }

    private suspend fun onMarkAttendance() {
        AttendanceBundle.upComingSession?.let {
            markAttendanceUseCase(it)
                .onSuccess { setEffect(MainContract.MainUiSideEffect.NavigateToBack) }
                .onFailure { showToast(resourcesProvider.getString(R.string.member_qr_move_back_to_home_and_retry_error_message)) }
        } ?: showToast(resourcesProvider.getString(R.string.member_qr_move_back_to_home_and_retry_error_message))
    }


    private fun showToast(text: String) {
        setState { copy(toastMessage = text) }
        setEffect(MainContract.MainUiSideEffect.ShowToast)
    }
}
