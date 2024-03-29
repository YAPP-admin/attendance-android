package com.yapp.presentation.ui.member.score

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.domain.model.Attendance
import com.yapp.domain.model.Session

class MemberScoreContract {
    data class MemberScoreUiState(
        val loadState: LoadState = LoadState.Loading,
        val attendanceList: List<Pair<Session, Attendance>> = emptyList(),
        val lastAttendanceList: List<Pair<Session, Attendance>> = emptyList(),
    ) : UiState {
        enum class LoadState {
            Loading, Idle, Error
        }
    }

    sealed class MemberScoreUiSideEffect : UiSideEffect {
    }

    sealed class MemberScoreUiEvent : UiEvent {
        object GetMemberScore : MemberScoreUiEvent()
    }
}
