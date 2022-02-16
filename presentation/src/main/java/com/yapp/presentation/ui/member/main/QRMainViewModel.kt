package com.yapp.presentation.ui.member.main

import com.yapp.common.base.BaseViewModel
import com.yapp.common.base.UiEvent
import com.yapp.common.util.FirebaseRemoteConfigs
import com.yapp.common.util.RemoteConfigData
import com.yapp.presentation.ui.member.main.state.QRMainContract.*

// hilt 추가해야함.
class QRMainViewModel(
    firebaseRemoteConfigs: FirebaseRemoteConfigs = FirebaseRemoteConfigs()
) : BaseViewModel<QRMainUiState, QRMainUiSideEffect, UiEvent>(QRMainUiState()) {

    init {
        firebaseRemoteConfigs.getValue(RemoteConfigData.MaginotlineTime) {
            setState { copy(time = it) }
        }
    }

    fun setSideEffect(effect: QRMainUiSideEffect) {
        setEffect { effect }
    }

    override fun handleEvent(event: UiEvent) {
        TODO("Not yet implemented")
    }
}

