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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList


class ManagementContract {

    private companion object {
        const val NOT_SELECTED = -1L
    }

    data class ManagementState(
        val loadState: LoadState = LoadState.Loading,
        val shared: Shared = Shared(),
        val attendanceStatisticalTableState: StatisticalTableLayoutState = StatisticalTableLayoutState(),
        val foldableItemStates: ImmutableList<FoldableItemLayoutState> = persistentListOf(),
        val topBarState: TopBarLayoutState = TopBarLayoutState(),
        val tabLayoutState: ManagementTabLayoutState = ManagementTabLayoutState.init(),
        val bottomSheetDialogState: ImmutableList<AttendanceBottomSheetItemLayoutState> = persistentListOf(),
    ) : UiState {

        data class ManagementTabLayoutState(
            private val itemsList: YDSTabLayoutItemStateList = YDSTabLayoutItemStateList(),
        ) {
            val items: List<YDSTabLayoutItemState>
                get() = itemsList.value

            val selectedIndex: Int
                get() = itemsList.selectedIndex

            fun select(index: Int): ManagementTabLayoutState {
                return this.copy(itemsList = itemsList.select(index))
            }

            companion object {
                const val LABEL_TEAM = "팀별"
                const val LABEL_POSITION = "직군별"

                const val INDEX_TEAM = 0
                const val INDEX_POSITION = 1

                fun init(): ManagementTabLayoutState {
                    return ManagementTabLayoutState(
                        itemsList = YDSTabLayoutItemStateList(
                            buildList {
                                add(
                                    INDEX_TEAM,
                                    YDSTabLayoutItemState(
                                        isSelected = true,
                                        label = LABEL_TEAM
                                    )
                                )

                                add(
                                    index = INDEX_POSITION,
                                    element = YDSTabLayoutItemState(
                                        isSelected = false,
                                        label = LABEL_POSITION
                                    )
                                )
                            }.toImmutableList()
                        )
                    )
                }
            }
        }

        data class Shared(val sessionId: Int = 0)

        data class TopBarLayoutState(val sessionTitle: String = "")

        enum class LoadState {
            Loading, Idle, Error
        }
    }

    sealed class ManagementEvent : UiEvent {
        data class OnTabItemSelected(val tabIndex: Int) : ManagementEvent()
        data class OnAttendanceTypeChanged(val memberId: Long, val attendanceType: Attendance.Status) : ManagementEvent()
    }

    sealed class ManagementSideEffect : UiSideEffect {
    }
}
