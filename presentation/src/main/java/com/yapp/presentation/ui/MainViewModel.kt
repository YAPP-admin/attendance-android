package com.yapp.presentation.ui

import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.CheckQrAuthTimeUseCase
import com.yapp.presentation.R
import com.yapp.presentation.common.AttendanceBundle
import com.yapp.presentation.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val resourcesProvider: ResourceProvider,
    private val checkQrAuthTime: CheckQrAuthTimeUseCase
) : BaseViewModel<MainContract.MainUiState, MainContract.MainUiSideEffect, MainContract.MainUiEvent>(
    MainContract.MainUiState()
) {

    override suspend fun handleEvent(event: MainContract.MainUiEvent) {
        when (event) {
            MainContract.MainUiEvent.OnClickQrAuthButton -> checkAttendanceValidate()
        }
    }

    private suspend fun checkAttendanceValidate() = coroutineScope {
        if (AttendanceBundle.isAbsent) {
            checkQrAuthTime()
                .onSuccess { isQRCheckEnable ->
                    if (isQRCheckEnable) {
                        setEffect(MainContract.MainUiSideEffect.NavigateToQRScreen)
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

    private fun showToast(text: String) {
        setState { copy(toastMessage = text) }
        setEffect(MainContract.MainUiSideEffect.ShowToast)
    }
}