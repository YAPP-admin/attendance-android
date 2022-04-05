package com.yapp.presentation.ui.member.signup.position

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.presentation.model.type.PositionType

class PositionContract {
    data class PositionUiState(
        val position: PositionType? = null
    ) : UiState

    sealed class PositionSideEffect : UiSideEffect {
        object NavigateToTeamScreen:PositionSideEffect()
    }

    sealed class PositionUiEvent:UiEvent{
        data class ChoosePosition(val position:PositionType) : PositionUiEvent()
        object ConfirmPosition : PositionUiEvent()
    }
}
