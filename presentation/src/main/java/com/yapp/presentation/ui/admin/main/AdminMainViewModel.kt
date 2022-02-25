package com.yapp.presentation.ui.admin.main

import com.google.gson.Gson
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.util.firebase.FirebaseRemoteConfig
import com.yapp.domain.util.firebase.RemoteConfigData
import com.yapp.presentation.ui.admin.main.AdminMainContract.*
import com.yapp.presentation.model.SessionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminMainViewModel @Inject constructor(
    firebaseRemoteConfig: FirebaseRemoteConfig
) : BaseViewModel<AdminMainUiState, AdminMainUiSideEffect, AdminMainUiEvent>(
    AdminMainUiState()
) {
    init {
        firebaseRemoteConfig.getValue(RemoteConfigData.SessionList) { json ->
            val sessions = Gson().fromJson(json, Array<SessionModel>::class.java).toList()
            setState { copy(isLoading = false, sessions = sessions) }
        }
    }

    override fun setEvent(event: AdminMainUiEvent) {
        super.setEvent(event)
    }

    override fun handleEvent(event: AdminMainUiEvent) {
        TODO("Not yet implemented")
    }
}
