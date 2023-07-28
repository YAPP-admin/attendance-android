package com.yapp.presentation.ui.admin.totalscore

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.ui.admin.management.components.foldableItem.FoldableItemState
import com.yapp.presentation.ui.admin.management.dto.ManagementTabLayoutState
import com.yapp.presentation.ui.admin.totalscore.dto.AdminTotalScoreSharedData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class AdminTotalScoreContract {

    data class AdminTotalScoreUiState(
        val shared: AdminTotalScoreSharedData = AdminTotalScoreSharedData(),
        val loadState: LoadState = LoadState.Loading,
        val tabLayoutState: ManagementTabLayoutState = ManagementTabLayoutState.init(),
        val foldableItemStates: ImmutableList<FoldableItemState> = persistentListOf()
    ) : UiState {

        enum class LoadState {
            Loading, Idle, Error
        }

    }

    sealed class AdminTotalScoreUiSideEffect : UiSideEffect {
        object NavigateToPreviousScreen : AdminTotalScoreUiSideEffect()
    }

    sealed class AdminTotalScoreUiEvent : UiEvent {
        object OnBackArrowClick : AdminTotalScoreUiEvent()
        class OnTabItemSelected(val tabIndex: Int) : AdminTotalScoreUiEvent()
        class OnPositionTypeHeaderItemClicked(val positionName: String) : AdminTotalScoreUiEvent()
        class OnTeamTypeHeaderItemClicked(val teamName: String, val teamNumber: Int) : AdminTotalScoreUiEvent()
    }
}