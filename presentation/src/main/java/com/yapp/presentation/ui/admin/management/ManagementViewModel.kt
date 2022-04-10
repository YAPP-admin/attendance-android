package com.yapp.presentation.ui.admin.management

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.usecases.GetAllMemberUseCase
import com.yapp.domain.usecases.SetMemberAttendanceUseCase
import com.yapp.presentation.model.AttendanceType
import com.yapp.presentation.model.AttendanceType.Companion.mapToEntity
import com.yapp.presentation.model.Member.Companion.mapTo
import com.yapp.presentation.ui.admin.management.ManagementContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagementViewModel @Inject constructor(
    private val getAllMemberUseCase: GetAllMemberUseCase,
    private val setMemberAttendanceUseCase: SetMemberAttendanceUseCase,
    private val savedStateHandle: SavedStateHandle

) : BaseViewModel<ManagementState, ManagementSideEffect, ManagementEvent>(ManagementState()) {

    companion object {
        const val DEFAULT_SESSION_ID = 0
    }

    init {
        setState {
            this.copy(sessionId = savedStateHandle.get<Int>("sessionId") ?: DEFAULT_SESSION_ID)
        }

        viewModelScope.launch {
            setLoading()
            getAllMemberState(sessionId = uiState.value.sessionId)
        }
    }

    override fun handleEvent(event: ManagementEvent) {
        when (event) {
            is ManagementEvent.OnDropDownButtonClicked -> {
                setState { this.copy(selectedMember = event.member) }
                setEffect(ManagementSideEffect.OpenBottomSheetDialog())
            }

            is ManagementEvent.OnExpandedClicked -> {
                setState {
                    val toggleExpanded = !this.teams[event.index].isExpanded
                    val oldState = this.teams.toMutableList()

                    oldState[event.index] = this.teams[event.index].copy(isExpanded = toggleExpanded)
                    val newState = oldState.toList()

                    return@setState this.copy(teams = newState)
                }
            }

            is ManagementEvent.OnAttendanceTypeChanged -> {
                uiState.value.selectedMember?.let { selectedMember ->
                    viewModelScope.launch {
                        changeMemberAttendance(
                            selectedMember = selectedMember,
                            changedAttendanceType = event.attendanceType,
                            sessionId = 0
                        )
                    }
                }
            }
        }
    }

    private suspend fun getAllMemberState(sessionId: Int) = coroutineScope {
        getAllMemberUseCase().collectWithCallback(
            onSuccess = { members ->
                val teams = members.map { it.mapTo() }
                    .groupBy { member -> member.team }
                    .map { (team, members) ->
                        ManagementState.TeamState(
                            isExpanded = false,
                            teamName = String.format("${team.type?.displayName} ${team.number}"),
                            members = members.map { member ->
                                ManagementState.MemberState(
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

                setState { this.copy(isLoading = false, memberCount = attendCount, teams = teams) }
            },
            onFailed = {
                setEffect(ManagementSideEffect.MemberListLoadFailed)
            }
        )
    }

    private suspend fun changeMemberAttendance(
        selectedMember: ManagementState.MemberState,
        changedAttendanceType: AttendanceType,
        sessionId: Int
    ) {
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
                setEffect(ManagementSideEffect.AttendanceChangeFailed)
            }
        )
    }

    private fun setLoading() {
        setState { this.copy(isLoading = true) }
    }

}