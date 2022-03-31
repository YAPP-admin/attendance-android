package com.yapp.presentation.ui.admin.totalscore

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

// 테스트용 임시 데이터 클래스(로직 구현하면서 삭제할 예정)
data class TeamItemState(
    val isExpanded: Boolean,
    val teamName: String,
    val teamMembers: List<MemberWithTotalScore>
)

data class MemberWithTotalScore(
    val name: String,
    val attendances: Int = 100
)

class AdminTotalScoreContract {
    data class AdminTotalScoreUiState(
        val isLoading: Boolean = false,
        val teamItemStates: List<TeamItemState> = emptyList()
    ) : UiState

    sealed class AdminTotalScoreUiSideEffect : UiSideEffect {

    }

    sealed class AdminTotalScoreUiEvent : UiEvent {
        class ClickTeamItem(val index: Int) : AdminTotalScoreUiEvent()
    }
}