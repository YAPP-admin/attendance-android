package com.yapp.presentation.ui.admin.browse

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState

// 테스트용 임시 데이터 클래스(로직 구현하면서 삭제할 예정)
data class MemberScore(
    val name: String,
    val attendances: Int = 100
)

class AdminTotalScoreContract {
    data class AdminTotalScoreUiState(
        val isLoading: Boolean = false,
        val teams: Map<String, List<MemberScore>> = emptyMap()
    ) : UiState

    sealed class AdminTotalScoreUiSideEffect : UiSideEffect {

    }

    sealed class AdminTotalScoreUiEvent : UiEvent {
    }
}