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
        val teams: List<TeamState> = emptyList()
    ): UiState {

        data class TeamState (
            val isExpanded: Boolean = false,
            val index: Int = -1,
            val teamName: String = "",
            val members: List<MemberState> = emptyList()
        )

        data class MemberState(
            val id: Long = 0L,
            val name: String = "",
            val attendance: Attendance = Attendance(sessionId = 0, attendanceType = Normal)
        )
    }

    sealed class ManagementEvent : UiEvent {
        class OnExpandedClicked(val team: ManagementState.TeamState): ManagementEvent()
        class OnDropDownButtonClicked(val member: ManagementState.MemberState) : ManagementEvent()
        class OnAttendanceTypeChanged(val attendanceType: AttendanceType) : ManagementEvent()
    }

    sealed class ManagementSideEffect : UiSideEffect {
        class OpenBottomSheetDialog : ManagementSideEffect()
    }
}