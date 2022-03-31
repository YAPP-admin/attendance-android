package com.yapp.presentation.ui.admin.attendance

import com.yapp.common.base.BaseViewModel
import com.yapp.presentation.model.Attendance
import com.yapp.presentation.model.AttendanceType
import com.yapp.presentation.ui.admin.attendance.ManagementContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManagementViewModel @Inject constructor(

) : BaseViewModel<ManagementState, ManagementSideEffect, ManagementEvent>(ManagementState()) {

    init {
        setState {
            ManagementState(
                isLoading = false,
                teams = listOf(
                    ManagementState.TeamState(
                        isExpanded = false,
                        teamName = "Android1",
                        members = listOf(
                            ManagementState.MemberState(
                                id = 1L,
                                name = "Jason",
                                attendance = Attendance(sessionId = 1, attendanceType = AttendanceType.Absent)
                            ),
                            ManagementState.MemberState(
                                id = 2L,
                                name = "John",
                                attendance = Attendance(sessionId = 1, attendanceType = AttendanceType.Absent)
                            ),
                            ManagementState.MemberState(
                                id = 3L,
                                name = "Kim",
                                attendance = Attendance(sessionId = 1, attendanceType = AttendanceType.Absent)
                            ),
                        )
                    ),
                    ManagementState.TeamState(
                        isExpanded = false,
                        teamName = "Android2",
                        members = listOf(
                            ManagementState.MemberState(
                                id = 4L,
                                name = "Zoey",
                                attendance = Attendance(sessionId = 1, attendanceType = AttendanceType.Normal)
                            ),
                            ManagementState.MemberState(
                                id = 5L,
                                name = "Joseph",
                                attendance = Attendance(sessionId = 1, attendanceType = AttendanceType.Normal)
                            ),
                            ManagementState.MemberState(
                                id = 6L,
                                name = "Jim",
                                attendance = Attendance(sessionId = 1, attendanceType = AttendanceType.Normal)
                            ),
                        )
                    )
                )
            )
        }
    }

    override fun handleEvent(event: ManagementEvent) {
        TODO("Not yet implemented")
    }

}