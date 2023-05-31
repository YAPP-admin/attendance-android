package com.yapp.presentation.ui.admin.management

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.model.Attendance
import com.yapp.domain.model.Member
import com.yapp.domain.model.Team
import com.yapp.domain.model.types.PositionType
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
import com.yapp.presentation.ui.admin.management.components.tablayout.YDSTabLayoutItemState
import com.yapp.presentation.ui.admin.management.components.tablayout.YDSTabLayoutItemStateList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagementViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAllMemberUseCase: GetAllMemberUseCase,
    private val setMemberAttendanceUseCase: SetMemberAttendanceUseCase
) : BaseViewModel<ManagementState, ManagementSideEffect, ManagementEvent>(ManagementState()) {

    companion object {
        const val DEFAULT_SESSION_ID = 1
        const val TEXT_LOAD_SESSION_TITLE_FAILED = "LOAD FAILED"
    }

    init {
        val sessionId = savedStateHandle.get<Int>(KEY_SESSION_ID) ?: error("세션 아이디를 불러올 수 없습니다.")
        val sessionTitle = savedStateHandle.get<String>(KEY_SESSION_TITLE) ?: TEXT_LOAD_SESSION_TITLE_FAILED

        viewModelScope.launch {
            getAllMemberUseCase().stateIn(
                this,
                started = SharingStarted.WhileSubscribed(3000L),
                initialValue = currentState
            )
            initializeState(sessionId, sessionTitle)
        }
    }

    override suspend fun handleEvent(event: ManagementEvent) {
        when(event) {
            is ManagementEvent.OnAttendanceTypeChanged -> TODO()
            is ManagementEvent.OnTabItemSelected -> {
                setState {
                    this.copy(tabLayoutState = tabLayoutState.select(event.tabIndex))
                }
            }
        }
    }

    private suspend fun initializeState(sessionId: Int, sessionTitle: String) {
        getAllMemberUseCase().collectLatest { result ->
            result.onSuccess { memberInfoList ->
                val foldableItemStateList = when(uiState.value.tabLayoutState.selectedIndex) {
                    ManagementState.ManagementTabLayoutState.INDEX_TEAM -> {
                        memberInfoList.groupBy { it.team }
                            .map { (team, members) ->
                                FoldableItemLayoutState(
                                    headerState = team.toFoldableItemHeaderState(sessionId, members),
                                    contentStates = members.map { member ->
                                        member.toFoldableItemContentState(sessionId = sessionId)
                                    }
                                )
                            }
                    }

                    ManagementState.ManagementTabLayoutState.INDEX_POSITION -> {
                        memberInfoList.groupBy { it.position }
                            .map { (position, members) ->
                                FoldableItemLayoutState(
                                    headerState = position.toFoldableItemHeaderState(sessionId, members),
                                    contentStates = members.map { member ->
                                        member.toFoldableItemContentState(sessionId = sessionId)
                                    }
                                )
                            }
                    }

                    else -> error("${uiState.value.tabLayoutState.selectedIndex}는 잘못된 Tablayout Index입니다.")
                }.toImmutableList()

                ManagementState(
                    loadState = ManagementState.LoadState.Idle,
                    shared = ManagementState.Shared(sessionId = sessionId),
                    attendanceStatisticalTableState = StatisticalTableLayoutState(
                        totalCount = memberInfoList.size,
                        attendCount = memberInfoList.count { it.attendances[sessionId].status == Attendance.Status.LATE },
                        tardyCount = memberInfoList.count { it.attendances[sessionId].status == Attendance.Status.LATE },
                        absentCount = memberInfoList.count { it.attendances[sessionId].status == Attendance.Status.ABSENT },
                        admitCount = memberInfoList.count { it.attendances[sessionId].status == Attendance.Status.ADMIT },
                    ),
                    foldableItemStates = foldableItemStateList,
                    tabLayoutState = ManagementState.ManagementTabLayoutState.init(),
                    bottomSheetDialogState = initBottomSheetDialogState(),
                    topBarState = ManagementState.TopBarLayoutState(sessionTitle = sessionTitle)
                ).also { initialState ->
                    setState { initialState }
                }

            }.onFailure {
                setState { this.copy(loadState = ManagementState.LoadState.Error) }
            }
        }
    }

    private fun initBottomSheetDialogState(): ImmutableList<AttendanceBottomSheetItemLayoutState> {
        return buildList {
            add(AttendanceBottomSheetItemLayoutState(label = Attendance.Status.NORMAL.text, iconType = AttendanceBottomSheetItemLayoutState.IconType.ATTEND))
            add(AttendanceBottomSheetItemLayoutState(label = Attendance.Status.LATE.text, iconType = AttendanceBottomSheetItemLayoutState.IconType.TARDY))
            add(AttendanceBottomSheetItemLayoutState(label = Attendance.Status.ABSENT.text, iconType = AttendanceBottomSheetItemLayoutState.IconType.ABSENT))
            add(AttendanceBottomSheetItemLayoutState(label = Attendance.Status.ADMIT.text, iconType = AttendanceBottomSheetItemLayoutState.IconType.ADMIT))
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
            iconType = when(attendance) {
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
