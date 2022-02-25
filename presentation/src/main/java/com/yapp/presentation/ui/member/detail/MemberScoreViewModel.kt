package com.yapp.presentation.ui.member.detail

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.util.firebase.FirebaseRemoteConfig
import com.yapp.domain.util.firebase.RemoteConfigData
import com.yapp.presentation.ui.member.detail.MemberScoreContract.*
import com.yapp.presentation.ui.member.main.MemberMainContract
import com.yapp.presentation.ui.model.SessionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MemberScoreViewModel @Inject constructor(
    firebaseRemoteConfig: FirebaseRemoteConfig
) : BaseViewModel<MemberScoreUiState, MemberScoreUiSideEffect, MemberScoreUiEvent>(
    MemberScoreUiState()
) {
    init {
        firebaseRemoteConfig.getValue(RemoteConfigData.SessionList) { json ->
            val sessions = Gson().fromJson(json, Array<SessionModel>::class.java).toList()
            setState { copy(isLoading = false, sessions = sessions) }
        }
    }

    override fun setEvent(event: MemberScoreUiEvent) {
        super.setEvent(event)
    }

    override fun handleEvent(event: MemberScoreUiEvent) {
        TODO("Not yet implemented")
    }
}