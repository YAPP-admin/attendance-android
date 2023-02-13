package com.yapp.presentation.ui.member.signup.position

import androidx.lifecycle.SavedStateHandle
import com.yapp.common.base.BaseViewModel
import com.yapp.presentation.ui.member.signup.position.PositionContract.PositionSideEffect
import com.yapp.presentation.ui.member.signup.position.PositionContract.PositionUiEvent
import com.yapp.presentation.ui.member.signup.position.PositionContract.PositionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PositionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<PositionUiState, PositionSideEffect, PositionUiEvent>(PositionUiState()) {
    override suspend fun handleEvent(event: PositionUiEvent) {
        when (event) {
            is PositionUiEvent.ChoosePosition -> {
                if (event.position == null) {
                    setEffect(PositionSideEffect.ShowToast("적합하지 않은 포지션을 선택하셨습니다.\n다시 선택해주세요."))
                    return
                }

                setState {
                    copy(ydsOption = ydsOption.select {
                        PositionContract.PositionOptionState(
                            selectedOption = event.position
                        )
                    })
                }
            }
            is PositionUiEvent.ConfirmPosition -> {
                val memberName = savedStateHandle.get<String>("name")
                setEffect(
                    PositionSideEffect.NavigateToTeamScreen(
                        memberName!!,
                        uiState.value.ydsOption.selectedOption!!
                    )
                )
            }
        }
    }
}
