package com.yapp.presentation.ui.admin.totalscore

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

class AdminTotalScoreContract {
    data class AdminTotalScoreUiState(
        val isLoading: Boolean = false,
        val teamItemStates: List<TeamItemState> = emptyList()
    ) : UiState {
        data class TeamItemState(
            val teamName: String,
            val teamMembers: List<MemberWithTotalScore>
        )

        data class MemberWithTotalScore(
            val name: String,
            val totalScore: Int = 100
        )
    }

    sealed class AdminTotalScoreUiSideEffect : UiSideEffect {
        class ShowToast(val msg: String) : AdminTotalScoreContract.AdminTotalScoreUiSideEffect()
    }

    sealed class AdminTotalScoreUiEvent : UiEvent {
    }
}