package com.yapp.presentation.ui.member.detail

import com.yapp.common.base.BaseViewModel
import com.yapp.presentation.ui.member.detail.MemberScoreContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MemberScoreViewModel @Inject constructor(
) : BaseViewModel<MemberScoreUiState, MemberScoreUiSideEffect, MemberScoreUiEvent>(
    MemberScoreUiState()
) {

    override fun setEvent(event: MemberScoreUiEvent) {
        super.setEvent(event)
    }

    override fun handleEvent(event: MemberScoreUiEvent) {
        TODO("Not yet implemented")
    }
}