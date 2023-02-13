package com.yapp.presentation.ui.member.signup.position

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.common.yds.YDSOptionState
import com.yapp.domain.model.types.PositionType

class PositionContract {
    data class PositionUiState(
        val ydsOption: YDSOptionState<PositionType> = PositionOptionState()
    ) : UiState

    sealed class PositionSideEffect : UiSideEffect {
        data class NavigateToTeamScreen(val name: String, val position: PositionType) :
            PositionSideEffect()

        data class ShowToast(val msg: String) : PositionSideEffect()
    }

    sealed class PositionUiEvent : UiEvent {
        data class ChoosePosition(val position: PositionType?) : PositionUiEvent()
        object ConfirmPosition : PositionUiEvent()
    }

    data class PositionOptionState(
        override val items: List<PositionType> = PositionType.values().toList(),
        override val selectedOption: PositionType? = null
    ) : YDSOptionState<PositionType>
}
