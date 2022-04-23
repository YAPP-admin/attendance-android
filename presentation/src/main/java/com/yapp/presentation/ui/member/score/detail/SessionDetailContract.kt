package com.yapp.presentation.ui.member.score.detail

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.model.Attendance
import com.yapp.presentation.model.Session
import com.yapp.presentation.ui.member.score.MemberScoreContract

class SessionDetailContract {
    data class SessionDetailUiState(
        val loadState :LoadState = LoadState.Idle,
        val session:Pair<Session, Attendance>? = null
    ) :UiState {
        enum class LoadState {
            Loading, Idle, Error
        }
    }
    sealed class SessionDetailUiSideEffect:UiSideEffect{}
    sealed class SessionDetailUiEvent : UiEvent {}

}