package com.yapp.presentation.ui.member.score.detail

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.common.yds.YDSAttendanceType

class SessionDetailContract {
    data class SessionDetailUiState(
        val loadState: LoadState = LoadState.Idle,
        val appBarTitle: String = "",
        val screenState: SessionDetailScreenState = SessionDetailScreenState()
    ) : UiState {
        enum class LoadState {
            Loading, Idle, Error
        }

        data class SessionDetailScreenState(
            val title: String = "",
            val description: String = "",
            val date: String = "",
            val attendanceType: YDSAttendanceType = YDSAttendanceType.ABSENT
        ) {

            val shouldShowIcon: Boolean
                get() = attendanceType in showingIconTypes

            private companion object {
                val showingIconTypes = listOf(YDSAttendanceType.ABSENT, YDSAttendanceType.ATTEND, YDSAttendanceType.TARDY)
            }
        }
    }

    sealed class SessionDetailUiSideEffect : UiSideEffect {}
    sealed class SessionDetailUiEvent : UiEvent {}

}