package com.yapp.presentation.ui.admin.management

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.model.Attendance
import com.yapp.presentation.model.AttendanceType
import com.yapp.presentation.model.AttendanceType.Normal


class ManagementContract {
    data class ManagementState(
        val loadState: LoadState = LoadState.Idle,
        val sessionId: Int = 0,
        val sessionTitle: String = "",
        val memberCount: Int = 0,
        val selectedMember: MemberState? = null,
        val teams: List<TeamState> = emptyList()
    ): UiState {

        enum class LoadState {
            Loading, Idle, Error
        }

        data class TeamState (
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
        class OnDropDownButtonClicked(val member: ManagementState.MemberState) : ManagementEvent()
        class OnAttendanceTypeChanged(val attendanceType: AttendanceType) : ManagementEvent()
    }

    sealed class ManagementSideEffect : UiSideEffect {
        class OpenBottomSheetDialog : ManagementSideEffect()
    }
}