package com.yapp.presentation.ui.member.score.detail

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.model.Attendance
import com.yapp.presentation.model.Session

class SessionDetailContract {
    data class SessionDetailUiState(
        val session:Pair<Session, Attendance>? = null
    ) :UiState
    sealed class SessionDetailUiSideEffect:UiSideEffect{}
    sealed class SessionDetailUiEvent : UiEvent {}

}