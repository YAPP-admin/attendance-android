package com.yapp.presentation.ui.admin.management

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.domain.model.Attendance
import com.yapp.presentation.ui.admin.management.components.attendanceBottomSheet.AttendanceBottomSheetItemLayoutState
import com.yapp.presentation.ui.admin.management.components.foldableItem.FoldableItemLayoutState
import com.yapp.presentation.ui.admin.management.components.statisticalTable.StatisticalTableLayoutState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


class ManagementContract {

    private companion object {
        const val NOT_SELECTED = -1L
    }

    data class ManagementState(
        val loadState: LoadState = LoadState.Idle,
        val shared: Shared = Shared(),
        val topBarState: TopBarLayoutState = TopBarLayoutState(),
        val attendanceStatisticalTableState: StatisticalTableLayoutState = StatisticalTableLayoutState(),
        val foldableItemStates: ImmutableList<FoldableItemLayoutState> = persistentListOf(),
        val bottomSheetDialogState: ImmutableList<AttendanceBottomSheetItemLayoutState> = persistentListOf(),
    ) : UiState {

        data class Shared(
            val sessionId: Int = 0,
            val selectedMemberId: Long = NOT_SELECTED,
        )

        data class TopBarLayoutState(val sessionTitle: String = "")

        enum class LoadState {
            Loading, Idle, Error
        }
    }

    sealed class ManagementEvent : UiEvent {
        data class OnDropDownButtonClicked(val memberId: Long) : ManagementEvent()
        data class OnAttendanceTypeChanged(val attendanceType: Attendance.Status) : ManagementEvent()
    }

    sealed class ManagementSideEffect : UiSideEffect {
        object OpenBottomSheetDialog : ManagementSideEffect()
    }
}
