package com.yapp.presentation.ui.admin.management

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.domain.model.Attendance
import com.yapp.presentation.ui.admin.management.components.attendanceBottomSheet.AttendanceBottomSheetItemLayoutState
import com.yapp.presentation.ui.admin.management.components.foldableItem.FoldableItemLayoutState
import com.yapp.presentation.ui.admin.management.components.statisticalTable.StatisticalTableLayoutState
import com.yapp.presentation.ui.admin.management.components.tablayout.YDSTabLayoutItemState
import com.yapp.presentation.ui.admin.management.components.tablayout.YDSTabLayoutItemStateList
import com.yapp.presentation.ui.admin.management.dto.ManagementSharedData
import com.yapp.presentation.ui.admin.management.dto.ManagementTabLayoutState
import com.yapp.presentation.ui.admin.management.dto.ManagementTopBarLayoutState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList


class ManagementContract {

    data class ManagementState(
        val shared: ManagementSharedData = ManagementSharedData(),
        val loadState: LoadState = LoadState.Loading,
        val topBarState: ManagementTopBarLayoutState = ManagementTopBarLayoutState(),
        val attendanceStatisticalTableState: StatisticalTableLayoutState = StatisticalTableLayoutState(),
        val tabLayoutState: ManagementTabLayoutState = ManagementTabLayoutState.init(),
        val foldableItemStates: ImmutableList<FoldableItemLayoutState> = persistentListOf(),
        val bottomSheetDialogState: ImmutableList<AttendanceBottomSheetItemLayoutState> = persistentListOf()
    ) : UiState {

        enum class LoadState {
            Loading, Idle, Error
        }

    }

    sealed class ManagementEvent : UiEvent {
        data class OnTabItemSelected(val tabIndex: Int) : ManagementEvent()
        data class OnAttendanceTypeChanged(val memberId: Long, val attendanceType: Attendance.Status) : ManagementEvent()
        data class OnDeleteMemberClicked(val memberId: Long) : ManagementEvent()
    }

    sealed class ManagementSideEffect : UiSideEffect
}
