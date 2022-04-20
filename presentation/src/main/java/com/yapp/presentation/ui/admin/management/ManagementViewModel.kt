package com.yapp.presentation.ui.admin.management

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.usecases.GetAllMemberUseCase
import com.yapp.domain.usecases.GetSessionListUseCase
import com.yapp.domain.usecases.SetMemberAttendanceUseCase
import com.yapp.presentation.model.AttendanceType
import com.yapp.presentation.model.AttendanceType.Companion.mapToEntity
import com.yapp.presentation.model.Member.Companion.mapTo
import com.yapp.presentation.model.Session.Companion.mapTo
import com.yapp.presentation.ui.admin.management.ManagementContract.*
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagementViewModel @Inject constructor(
    private val getAllMemberUseCase: GetAllMemberUseCase,
    private val setMemberAttendanceUseCase: SetMemberAttendanceUseCase,
    savedStateHandle: SavedStateHandle

) : BaseViewModel<ManagementState, ManagementSideEffect, ManagementEvent>(ManagementState()) {

    companion object {
        const val KEY_SESSION_ID = "sessionId"
        const val KEY_SESSION_TITLE = "sessionTitle"

        const val DEFAULT_SESSION_ID = 1
        const val DEFAULT_SESSION_TITLE = "YAPP"
    }

    init {
        val sessionId    = savedStateHandle.get<Int>(KEY_SESSION_ID) ?: DEFAULT_SESSION_ID
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
                setEffect(ManagementSideEffect.OpenBottomSheetDialog())
            }

            is ManagementEvent.OnAttendanceTypeChanged -> {
                uiState.value.selectedMember?.let { selectedMember ->
                    changeMemberAttendance(selectedMember = selectedMember, changedAttendanceType = event.attendanceType, sessionId = 0)
                }
            }
        }
    }

    private suspend fun getAllMemberState(sessionId: Int) = coroutineScope {
        getAllMemberUseCase().collectWithCallback(
            onSuccess = { memberEntities ->
                val teams = memberEntities.map { entity -> entity.mapTo() }
                    .groupBy { member -> member.team }
                    .map { (team, members) ->
                        TeamState(
                            teamName = String.format("${team.type?.displayName} ${team.number}팀"),
                            members = members.map { member ->
                                MemberState(
                                    id = member.id,
                                    name = member.name,
                                    attendance = member.attendances.getAttendanceBySessionId(sessionId = sessionId)
                                )
                            }
                        )
                    }
                    .sortedBy { teamState -> teamState.teamName }

                val attendCount = teams.flatMap { it.members }
                    .count { it.attendance.attendanceType == AttendanceType.Normal }

                setState { this.copy(loadState = LoadState.Idle, memberCount = attendCount, teams = teams) }
            },
            onFailed = {
                setState { this.copy(loadState = LoadState.Error) }
            }
        )
    }

    private suspend fun changeMemberAttendance(
        selectedMember: MemberState,
        changedAttendanceType: AttendanceType,
        sessionId: Int
    ) {
        if (selectedMember.attendance.attendanceType == changedAttendanceType) {
            return
        }

        setMemberAttendanceUseCase(
            params = SetMemberAttendanceUseCase.Params(
                memberId = selectedMember.id,
                sessionId = sessionId,
                changedAttendance = AttendanceEntity(sessionId = sessionId, type = changedAttendanceType.mapToEntity())
            )
        ).collectWithCallback(
            onSuccess = {
                setState { this.copy(selectedMember = null) }
                getAllMemberState(sessionId = sessionId)
            },
            onFailed = {
                setState { this.copy(loadState = LoadState.Error) }
            }
        )
    }

}