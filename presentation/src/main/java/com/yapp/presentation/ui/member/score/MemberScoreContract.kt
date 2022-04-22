package com.yapp.presentation.ui.member.score

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.model.Attendance
import com.yapp.presentation.model.Session

class MemberScoreContract {
    data class MemberScoreUiState(
        val isLoading: Boolean = true,
        val attendanceList: List<Pair<Session, Attendance>> = emptyList(),
        val lastAttendanceList: List<Pair<Session, Attendance>> = emptyList()
    ) : UiState

    sealed class MemberScoreUiSideEffect : UiSideEffect {
    }

    sealed class MemberScoreUiEvent : UiEvent {
    }
}
