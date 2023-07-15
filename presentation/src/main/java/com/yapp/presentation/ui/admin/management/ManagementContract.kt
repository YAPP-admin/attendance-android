package com.yapp.presentation.ui.admin.management

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.domain.model.Attendance
import com.yapp.presentation.ui.admin.management.components.attendanceBottomSheet.AttendanceBottomSheetItemLayoutState
import com.yapp.presentation.ui.admin.management.components.foldableItem.FoldableItemState
import com.yapp.presentation.ui.admin.management.components.statisticalTable.StatisticalTableLayoutState
import com.yapp.presentation.ui.admin.management.dto.ManagementSharedData
import com.yapp.presentation.ui.admin.management.dto.ManagementTabLayoutState
import com.yapp.presentation.ui.admin.management.dto.ManagementTopBarLayoutState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


class ManagementContract {

    data class ManagementState(
        val shared: ManagementSharedData = ManagementSharedData(),
        val loadState: LoadState = LoadState.Loading,
        val topBarState: ManagementTopBarLayoutState = ManagementTopBarLayoutState(),
        val attendanceStatisticalTableState: StatisticalTableLayoutState = StatisticalTableLayoutState(),
        val tabLayoutState: ManagementTabLayoutState = ManagementTabLayoutState.init(),
        val foldableItemStates: ImmutableList<FoldableItemState> = persistentListOf(),
        val bottomSheetDialogState: ImmutableList<AttendanceBottomSheetItemLayoutState> = persistentListOf()
    ) : UiState {

        enum class LoadState {
            Loading, Idle, Error
        }

    }

    sealed class ManagementEvent : UiEvent {
        class OnTabItemSelected(val tabIndex: Int) : ManagementEvent()
        class OnAttendanceTypeChanged(val memberId: Long, val attendanceType: Attendance.Status) : ManagementEvent()
        class OnDeleteMemberClicked(val memberId: Long) : ManagementEvent()
        class OnPositionTypeHeaderItemClicked(val positionName: String) : ManagementEvent()
        class OnTeamTypeHeaderItemClicked(val teamName: String, val teamNumber: Int) : ManagementEvent()
    }

    sealed class ManagementSideEffect : UiSideEffect
}
