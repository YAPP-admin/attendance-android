package com.yapp.presentation.ui.admin.management

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.model.Attendance
import com.yapp.domain.model.Member
import com.yapp.domain.model.Team
import com.yapp.domain.model.types.AttendanceType
import com.yapp.domain.usecases.GetAllMemberUseCase
import com.yapp.domain.usecases.SetMemberAttendanceUseCase
import com.yapp.presentation.ui.admin.AdminConstants.KEY_SESSION_ID
import com.yapp.presentation.ui.admin.AdminConstants.KEY_SESSION_TITLE
import com.yapp.presentation.ui.admin.management.ManagementContract.*
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagementViewModel @Inject constructor(
    private val getAllMemberUseCase: GetAllMemberUseCase,
    private val setMemberAttendanceUseCase: SetMemberAttendanceUseCase,
    savedStateHandle: SavedStateHandle,

    ) : BaseViewModel<ManagementState, ManagementSideEffect, ManagementEvent>(ManagementState()) {

    companion object {
        const val DEFAULT_SESSION_ID = 1
        const val DEFAULT_SESSION_TITLE = "YAPP"
    }

    init {
        val sessionId = savedStateHandle.get<Int>(KEY_SESSION_ID) ?: DEFAULT_SESSION_ID
        val sessionTitle = savedStateHandle.get<String>(KEY_SESSION_TITLE) ?: DEFAULT_SESSION_TITLE
        setState { this.copy(sessionId = sessionId, sessionTitle = sessionTitle) }

        viewModelScope.launch {
            setState { this.copy(loadState = LoadState.Loading) }
            getAllMemberState(sessionId = uiState.value.sessionId)
        }
    }

    override suspend fun handleEvent(event: ManagementEvent) {
        when (event) {
            is ManagementEvent.OnDropDownButtonClicked -> {
                setState { this.copy(selectedMember = event.member) }
                setEffect(ManagementSideEffect.OpenBottomSheetDialog)
            }

            is ManagementEvent.OnAttendanceTypeChanged -> {
                uiState.value.selectedMember?.let { selectedMember ->
                    changeMemberAttendance(
                        selectedMember = selectedMember,
                        changedAttendanceType = event.attendanceType,
                        sessionId = uiState.value.sessionId
                    )
                }
            }
        }
    }

    private suspend fun getAllMemberState(sessionId: Int) {
        getAllMemberUseCase().collect { result ->
            result.onSuccess { members ->
                val membersByTeam = members.groupBy { member -> member.team }
                    .map { (team, members) -> mapToTeamState(team, members, sessionId) }
                    .sortedBy { teamState -> teamState.teamName }

                val attendCount = membersByTeam.flatMap { it.members }
                    .count { it.attendance.type == AttendanceType.Normal }

                setState {
                    this.copy(
                        loadState = LoadState.Idle,
                        memberCount = attendCount,
                        teams = membersByTeam
                    )
                }
            }.onFailure {
                setState { this.copy(loadState = LoadState.Error) }
            }
        }
    }

    private fun mapToTeamState(team: Team, members: List<Member>, sessionId: Int): TeamState {
        return TeamState(
            teamName = String.format("${team.type.value} ${team.number}팀"),
            members = members.sortedWith(
                compareBy<Member> { it.attendances.getAttendanceBySessionId(sessionId).type.order }
                    .thenBy { it.name }
            ).map { member ->
                MemberState(
                    id = member.id,
                    name = member.name,
                    attendance = member.attendances.getAttendanceBySessionId(sessionId = sessionId)
                )
            }
        )
    }

    private suspend fun changeMemberAttendance(
        selectedMember: MemberState,
        changedAttendanceType: AttendanceType,
        sessionId: Int,
    ) {
        if (selectedMember.attendance.type == changedAttendanceType) {
            return
        }
        setMemberAttendanceUseCase(
            params = SetMemberAttendanceUseCase.Params(
                memberId = selectedMember.id,
                sessionId = sessionId,
                changedAttendance = Attendance(sessionId = sessionId, type = changedAttendanceType)
            )
        ).onSuccess {
            setState { this.copy(selectedMember = null) }
            getAllMemberState(sessionId = sessionId)
        }.onFailure {
            setState { this.copy(loadState = LoadState.Error) }
        }
    }

}
