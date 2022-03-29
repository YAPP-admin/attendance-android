package com.yapp.presentation.ui.admin.attendance

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.model.Attendance
import com.yapp.presentation.model.AttendanceType
import com.yapp.presentation.model.AttendanceType.Normal


class ManagementContract {
    data class ManagementState(
        val isLoading: Boolean = false,
        val teams: List<TeamItem> = emptyList()
    ): UiState {

        data class TeamItem (
            val isExpanded: Boolean = false,
            val teamName: String = "",
            val members: List<MemberItem> = emptyList()
        )

        data class MemberItem(
            val id: Long = 0L,
            val name: String = "",
            val attendance: Attendance = Attendance(sessionId = 0, attendanceType = Normal)
        )
    }

    sealed class ManagementEvent : UiEvent {
        class OnExpandedClicked(team: ManagementState.TeamItem): ManagementEvent()
        class OnChangedAttendanceType(member: ManagementState.MemberItem, changedAttendanceType: AttendanceType) : ManagementEvent()
    }

    sealed class ManagementSideEffect : UiSideEffect
}