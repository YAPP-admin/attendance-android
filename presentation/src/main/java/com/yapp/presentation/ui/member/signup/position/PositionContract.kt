package com.yapp.presentation.ui.member.signup.position

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.domain.model.types.PositionType

class PositionContract {
    data class PositionUiState(
        val position: PositionType? = null,
    ) : UiState

    sealed class PositionSideEffect : UiSideEffect {
        data class NavigateToTeamScreen(val name: String, val position: PositionType) : PositionSideEffect()
    }

    sealed class PositionUiEvent : UiEvent {
        data class ChoosePosition(val position: PositionType) : PositionUiEvent()
        object ConfirmPosition : PositionUiEvent()
    }
}
