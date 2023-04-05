package com.yapp.presentation.ui.admin.totalscore

import androidx.compose.runtime.Stable
import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.domain.model.collections.AttendanceList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class AdminTotalScoreContract {

    @Stable
    data class AdminTotalScoreUiState(
        val loadState: LoadState = LoadState.Idle,
        val sectionItemStates: ImmutableList<SectionItemState> = persistentListOf(),
        val sectionType: SectionType = SectionType.Team,
        val lastSessionId: Int = AttendanceList.DEFAULT_UPCOMING_SESSION_ID,
    ) : UiState {
        enum class LoadState {
            Loading, Idle, Error
        }

        @Stable
        data class SectionItemState(
            val sectionName: String,
            val members: ImmutableList<MemberState>,
        )

        data class MemberState(
            val name: String,
            val totalScore: Int = 100,
        )

        enum class SectionType {
            Team, Position
        }
    }

    sealed class AdminTotalScoreUiSideEffect : UiSideEffect {
        object NavigateToPreviousScreen : AdminTotalScoreUiSideEffect()
    }

    sealed class AdminTotalScoreUiEvent : UiEvent {
        object OnBackArrowClick : AdminTotalScoreUiEvent()
        class OnSectionTypeChange(val sectionType: AdminTotalScoreUiState.SectionType) :
            AdminTotalScoreUiEvent()
    }
}