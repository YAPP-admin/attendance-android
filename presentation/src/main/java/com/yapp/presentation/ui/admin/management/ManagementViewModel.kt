package com.yapp.presentation.ui.admin.management

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetAllMemberUseCase
import com.yapp.presentation.model.Attendance
import com.yapp.presentation.model.AttendanceType
import com.yapp.presentation.model.Member.Companion.mapTo
import com.yapp.presentation.ui.admin.management.ManagementContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagementViewModel @Inject constructor(
    private val getAllMemberUseCase: GetAllMemberUseCase

) : BaseViewModel<ManagementState, ManagementSideEffect, ManagementEvent>(ManagementState()) {

    init {
        viewModelScope.launch {
            getAllMemberState(0)
        }

//        setState {
//            ManagementState(
//                isLoading = false,
//                teams = listOf(
//                    ManagementState.TeamState(
//                        isExpanded = false,
//                        teamName = "Android1",
//                        members = listOf(
//                            ManagementState.MemberState(
//                                id = 1L,
//                                name = "Jason",
//                                attendance = Attendance(sessionId = 1, attendanceType = AttendanceType.Absent)
//                            ),
//                            ManagementState.MemberState(
//                                id = 2L,
//                                name = "John",
//                                attendance = Attendance(sessionId = 1, attendanceType = AttendanceType.Absent)
//                            ),
//                            ManagementState.MemberState(
//                                id = 3L,
//                                name = "Kim",
//                                attendance = Attendance(sessionId = 1, attendanceType = AttendanceType.Absent)
//                            ),
//                        )
//                    ),
//                    ManagementState.TeamState(
//                        isExpanded = false,
//                        teamName = "Android2",
//                        members = listOf(
//                            ManagementState.MemberState(
//                                id = 4L,
//                                name = "Zoey",
//                                attendance = Attendance(sessionId = 1, attendanceType = AttendanceType.Normal)
//                            ),
//                            ManagementState.MemberState(
//                                id = 5L,
//                                name = "Joseph",
//                                attendance = Attendance(sessionId = 1, attendanceType = AttendanceType.Normal)
//                            ),
//                            ManagementState.MemberState(
//                                id = 6L,
//                                name = "Jim",
//                                attendance = Attendance(sessionId = 1, attendanceType = AttendanceType.Normal)
//                            ),
//                        )
//                    )
//                )
//            )
//        }
    }

    override fun handleEvent(event: ManagementEvent) {
        when (event) {
            is ManagementEvent.OnDropDownButtonClicked -> {
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
                            teamName = team.type?.displayName + team.number,
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

                setState { this.copy(isLoading = false, teams = teams) }
            },
            onFailed = {
                setEffect(ManagementSideEffect.MemberListLoadFailed)
            }
        )
    }

}