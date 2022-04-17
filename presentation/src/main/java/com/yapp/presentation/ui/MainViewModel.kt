package com.yapp.presentation.ui

import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.CheckQrAuthTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkQrAuthTime: CheckQrAuthTime

) : BaseViewModel<MainContract.MainUiState, MainContract.MainUiSideEffect, MainContract.MainUiEvent>(MainContract.MainUiState) {

    override suspend fun handleEvent(event: MainContract.MainUiEvent) {
        when (event) {
            MainContract.MainUiEvent.OnClickQrAuthButton -> { checkAttendanceValidate() }
        }
    }

    private suspend fun checkAttendanceValidate() = coroutineScope {
        checkQrAuthTime()
            .collectWithCallback(
                onSuccess = { isEnable ->
                    if (isEnable) {
                        setEffect(MainContract.MainUiSideEffect.NavigateToQRScreen)
                    } else {
                        setEffect(MainContract.MainUiSideEffect.ShowToast)
                    }
                },
                onFailed = {
                    setEffect(MainContract.MainUiSideEffect.ShowToast)
                }
            )
    }
}