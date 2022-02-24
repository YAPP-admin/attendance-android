package com.yapp.presentation.ui.member.main

import com.yapp.common.base.BaseViewModel
import com.yapp.domain.util.firebase.FirebaseRemoteConfig
import com.yapp.domain.util.firebase.RemoteConfigData
import com.yapp.presentation.ui.member.main.QRMainContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QRMainViewModel @Inject constructor(
    firebaseRemoteConfigProvider: FirebaseRemoteConfig
) : BaseViewModel<QRMainUiState, QRMainUiSideEffect, QRMainUiEvent>(QRMainUiState()) {

    init {
        firebaseRemoteConfigProvider.getValue(RemoteConfigData.MaginotlineTime) {
            setState { copy(time = it) }
        }
    }

    override fun handleEvent(event: QRMainUiEvent) {
        when (event) {
            is QRMainUiEvent.OnSnackBarButtonClicked -> {
                setEffect(QRMainUiSideEffect.ShowToast("클릭!"))
            }
            is QRMainUiEvent.OnDialogButtonClicked -> {
                setState { copy(showDialog = true) }
            }

            is QRMainUiEvent.CloseDialog -> {
                setState { copy(showDialog = false) }
            }

            is QRMainUiEvent.OnClickSelectableButtonClicked -> {
                setState { copy(selectedButtonId = event.num) }
            }
        }
    }
}

