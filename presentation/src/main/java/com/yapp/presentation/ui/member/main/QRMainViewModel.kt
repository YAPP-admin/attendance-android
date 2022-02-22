package com.yapp.presentation.ui.member.main

import com.yapp.common.base.BaseViewModel
import com.yapp.common.util.FirebaseRemoteConfigs
import com.yapp.common.util.RemoteConfigData
import com.yapp.presentation.ui.member.main.state.QRMainContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QRMainViewModel @Inject constructor(
    firebaseRemoteConfigs: FirebaseRemoteConfigs
) : BaseViewModel<QRMainUiState, QRMainUiSideEffect, QRMainUiEvent>(QRMainUiState()) {

    init {
        firebaseRemoteConfigs.getValue(RemoteConfigData.MaginotlineTime) {
            setState { copy(time = it) }
        }
    }

    override fun handleEvent(event: QRMainUiEvent) {
        when (event) {
            is QRMainUiEvent.OnButtonClicked -> {
                setEffect(QRMainUiSideEffect.ShowToast)
            }
        }
    }
}

