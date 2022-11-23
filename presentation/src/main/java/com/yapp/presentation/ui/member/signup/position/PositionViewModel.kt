package com.yapp.presentation.ui.member.signup.position

import androidx.lifecycle.SavedStateHandle
import com.yapp.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PositionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<PositionContract.PositionUiState, PositionContract.PositionSideEffect, PositionContract.PositionUiEvent>(PositionContract.PositionUiState()) {
    override suspend fun handleEvent(event: PositionContract.PositionUiEvent) {
        when (event) {
            is PositionContract.PositionUiEvent.ChoosePosition -> {
                setState { copy(position = event.position) }
            }
            is PositionContract.PositionUiEvent.ConfirmPosition -> {
                val memberName = savedStateHandle.get<String>("name").orEmpty()

                if(memberName.isEmpty()) {
                    setEffect(PositionContract.PositionSideEffect.NavigateToNameScreen(NameNotFoundError))
                } else {
                    setEffect(PositionContract.PositionSideEffect.NavigateToTeamScreen(memberName, uiState.value.position!! ))
                }

            }
        }

    }
}