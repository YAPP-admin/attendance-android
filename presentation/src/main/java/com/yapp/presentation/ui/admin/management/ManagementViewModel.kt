package com.yapp.presentation.ui.admin.management

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.model.Attendance
import com.yapp.domain.model.Member
import com.yapp.domain.model.Team
import com.yapp.domain.model.types.PositionType
import com.yapp.domain.usecases.DeleteMemberInfoUseCase
import com.yapp.domain.usecases.GetAllMemberUseCase
import com.yapp.domain.usecases.SetMemberAttendanceUseCase
import com.yapp.presentation.ui.admin.AdminConstants.KEY_SESSION_ID
import com.yapp.presentation.ui.admin.AdminConstants.KEY_SESSION_TITLE
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementEvent
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementSideEffect
import com.yapp.presentation.ui.admin.management.ManagementContract.ManagementState
import com.yapp.presentation.ui.admin.management.components.attendanceBottomSheet.AttendanceBottomSheetItemLayoutState
import com.yapp.presentation.ui.admin.management.components.attendanceTypeButton.AttendanceTypeButtonState
import com.yapp.presentation.ui.admin.management.components.foldableContentItem.FoldableItemContentLayoutState
import com.yapp.presentation.ui.admin.management.components.foldableHeaderItem.FoldableItemHeaderLayoutState
import com.yapp.presentation.ui.admin.management.components.foldableItem.FoldableItemLayoutState
import com.yapp.presentation.ui.admin.management.components.statisticalTable.StatisticalTableLayoutState
import com.yapp.presentation.ui.admin.management.dto.ManagementSharedData
import com.yapp.presentation.ui.admin.management.dto.ManagementTabLayoutState
import com.yapp.presentation.ui.admin.management.dto.ManagementTopBarLayoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagementViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getAllMemberUseCase: GetAllMemberUseCase,
    private val setMemberAttendanceUseCase: SetMemberAttendanceUseCase,
    private val deleteMemberInfoUseCase: DeleteMemberInfoUseCase
) : BaseViewModel<ManagementState, ManagementSideEffect, ManagementEvent>(ManagementState()) {

    companion object {
        const val TEXT_LOAD_SESSION_TITLE_FAILED = "LOAD FAILED"
    }

    private val members: StateFlow<List<Member>> = getAllMemberUseCase()
        .catch { setState { this.copy(loadState = ManagementState.LoadState.Error) } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000L),
            initialValue = emptyList()
        )

    private val tabIndex: MutableStateFlow<Int> = MutableStateFlow(uiState.value.tabLayoutState.selectedIndex)

    init {
        val sessionId = savedStateHandle.get<Int>(KEY_SESSION_ID) ?: error("세션 아이디를 불러올 수 없습니다.")
        val sessionTitle = savedStateHandle.get<String>(KEY_SESSION_TITLE) ?: TEXT_LOAD_SESSION_TITLE_FAILED

        initializeState(sessionId, sessionTitle)

        /**
         * members변경 이외에도 tabIndex가 변경 될 시 UiState를 Update해준다.
         */
        viewModelScope.launch {
            members.combine(tabIndex, transform = { members, tabIndex ->
                members to tabIndex
            }).collect { (members, tabIndex) ->
                setState {
                    this.copy(
                        attendanceStatisticalTableState = members.toStatisticalTableState(sessionId = sessionId),
                        foldableItemStates = members.toFoldableItemContentState(sessionId, tabIndex)
                    )
                }
            }
        }
    }

    override suspend fun handleEvent(event: ManagementEvent) {
        when (event) {
            is ManagementEvent.OnAttendanceTypeChanged -> {
                setMemberAttendanceUseCase(
                    params = SetMemberAttendanceUseCase.Params(
                        memberId = event.memberId,
                        sessionId = uiState.value.shared.sessionId,
                        changedAttendance = event.attendanceType
                    )
                )
            }

            is ManagementEvent.OnTabItemSelected -> {
                setState {
                    tabIndex.value = event.tabIndex
                    this.copy(tabLayoutState = tabLayoutState.select(event.tabIndex))
                }
            }

            is ManagementEvent.OnDeleteMemberClicked -> {
                deleteMemberInfoUseCase(event.memberId)
            }
        }
    }

    private fun initializeState(sessionId: Int, sessionTitle: String) {
        setState {
            this.copy(
                loadState = ManagementState.LoadState.Idle,
                shared = ManagementSharedData(sessionId = sessionId),
                topBarState = ManagementTopBarLayoutState(sessionTitle = sessionTitle),
                tabLayoutState = ManagementTabLayoutState.init(),
                bottomSheetDialogState = AttendanceBottomSheetItemLayoutState.init()
            )
        }
    }

    private fun List<Member>.toStatisticalTableState(sessionId: Int): StatisticalTableLayoutState {
        return StatisticalTableLayoutState(
            totalCount = size,
            attendCount = count { it.attendances[sessionId].status == Attendance.Status.NORMAL },
            tardyCount = count { it.attendances[sessionId].status == Attendance.Status.LATE },
            absentCount = count { it.attendances[sessionId].status == Attendance.Status.ABSENT },
            admitCount = count { it.attendances[sessionId].status == Attendance.Status.ADMIT },
        )
    }

    private fun List<Member>.toFoldableItemContentState(sessionId: Int, tabIndex: Int): ImmutableList<FoldableItemLayoutState> {
        return when (tabIndex) {
            ManagementTabLayoutState.INDEX_TEAM -> {
                this.groupBy { it.team }
                    .entries.sortedWith(comparator = compareBy({ it.key.type }, { it.key.number }))
                    .map { (team, members) ->
                        FoldableItemLayoutState(
                            headerState = team.toFoldableItemHeaderState(sessionId, members),
                            contentStates = members
                                .sortedBy { it.attendances[sessionId].status }
                                .map { member -> member.toFoldableItemContentState(sessionId = sessionId) }
                                .toImmutableList()
                        )
                    }
            }

            ManagementTabLayoutState.INDEX_POSITION -> {
                this.groupBy { it.position }
                    .entries.sortedBy { it.key.value }
                    .map { (position, members) ->
                        FoldableItemLayoutState(
                            headerState = position.toFoldableItemHeaderState(sessionId, members),
                            contentStates = members
                                .sortedBy { it.attendances[sessionId].status }
                                .map { member -> member.toFoldableItemContentState(sessionId = sessionId) }
                                .toImmutableList()
                        )
                    }
            }

            else -> error("${uiState.value.tabLayoutState.selectedIndex}는 잘못된 Tablayout Index입니다.")
        }.toImmutableList()
    }

    private fun PositionType.toFoldableItemHeaderState(sessionId: Int, members: List<Member>): FoldableItemHeaderLayoutState {
        return FoldableItemHeaderLayoutState(
            label = value,
            attendMemberCount = members.count { it.attendances[sessionId].status == Attendance.Status.NORMAL },
            allTeamMemberCount = members.size
        )
    }

    private fun Team.toFoldableItemHeaderState(sessionId: Int, members: List<Member>): FoldableItemHeaderLayoutState {
        return FoldableItemHeaderLayoutState(
            label = "${type.value} $number",
            attendMemberCount = members.count { it.attendances[sessionId].status == Attendance.Status.NORMAL },
            allTeamMemberCount = members.size
        )
    }

    private fun Member.toFoldableItemContentState(sessionId: Int): FoldableItemContentLayoutState {
        val attendance = attendances[sessionId].status

        val buttonState = AttendanceTypeButtonState(
            label = attendance.text,
            iconType = when (attendance) {
                Attendance.Status.ABSENT -> AttendanceTypeButtonState.IconType.ABSENT
                Attendance.Status.LATE -> AttendanceTypeButtonState.IconType.TARDY
                Attendance.Status.ADMIT -> AttendanceTypeButtonState.IconType.ADMIT
                Attendance.Status.NORMAL -> AttendanceTypeButtonState.IconType.ATTEND
            }
        )

        return FoldableItemContentLayoutState(
            id = id,
            label = name,
            subLabel = position.value,
            attendanceTypeButtonState = buttonState
        )
    }

}
