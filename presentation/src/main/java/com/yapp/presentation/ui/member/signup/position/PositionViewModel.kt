package com.yapp.presentation.ui.member.signup.position

import androidx.lifecycle.SavedStateHandle
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.SignUpMemberUseCase
import com.yapp.presentation.ui.member.signup.position.PositionContract.PositionSideEffect
import com.yapp.presentation.ui.member.signup.position.PositionContract.PositionUiEvent
import com.yapp.presentation.ui.member.signup.position.PositionContract.PositionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PositionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val signUpMemberUseCase: SignUpMemberUseCase,
) : BaseViewModel<PositionUiState, PositionSideEffect, PositionUiEvent>(PositionUiState()) {
    override suspend fun handleEvent(event: PositionUiEvent) {
        when (event) {
            is PositionUiEvent.ChoosePosition -> {
                if (event.position == null) {
                    setEffect(PositionSideEffect.ShowToast(SELECT_UNSUITABLE_POSITION))
                    return
                }

                setState {
                    copy(ydsOption = PositionContract.PositionOptionState(selectedOption = event.position))
                }
            }

            is PositionUiEvent.ConfirmPosition -> {
                signUpMember()
            }
        }
    }

    private suspend fun signUpMember() {
        val name = savedStateHandle.get<String>("name") ?: throw IllegalStateException(NAME_IS_NULL)
        val position = currentState.ydsOption.selectedOption ?: throw IllegalStateException(POSITION_IS_NULL)
        signUpMemberUseCase(
            params = SignUpMemberUseCase.Params(
                memberName = name,
                memberPosition = position
            )
        ).onSuccess {
            setEffect(PositionSideEffect.NavigateToMainScreen)
        }.onFailure {
            setEffect(PositionSideEffect.ShowToast(SIGN_UP_FAILED))
        }
    }

    companion object {
        private const val SELECT_UNSUITABLE_POSITION = "적합하지 않은 포지션을 선택하셨습니다.\n다시 선택해주세요."
        private const val NAME_IS_NULL = "name is null"
        private const val POSITION_IS_NULL = "position is null"
        private const val SIGN_UP_FAILED = "회원가입 실패"
    }
}
