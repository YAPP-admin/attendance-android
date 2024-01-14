package com.yapp.presentation.ui.admin.createsession

import com.yapp.common.base.UiEvent
import com.yapp.common.base.UiSideEffect
import com.yapp.common.base.UiState
import com.yapp.domain.model.types.NeedToAttendType

class CreateSessionContract {
    data class CreateSessionUiState(
        val loadState: LoadState = LoadState.Idle,
        val showDialog: Boolean = false,
        val title: String = "",
        val type: NeedToAttendType? = null,
        val startTime: String = "",
        val code: String = "",
        val description: String = "",
    ) : UiState {
        val enableCreate: Boolean
            get() {
                if (title.isNotBlank() and (type != null) and startTime.isNotBlank() and code.isNotBlank()) {
                    return true
                }

                return false
            }

        enum class LoadState {
            Loading, Idle
        }
    }

    sealed class CreateSessionUiSideEffect : UiSideEffect {
        object NavigateToPreviousScreen : CreateSessionUiSideEffect()
        object KeyboardHide : CreateSessionUiSideEffect()
        object ShowBottomSheet : CreateSessionUiSideEffect()
        object HideBottomSheet : CreateSessionUiSideEffect()
        data class ShowToast(val message: String) : CreateSessionUiSideEffect()
    }

    sealed class CreateSessionUiEvent : UiEvent {
        object OnScreenClick : CreateSessionUiEvent()
        object OnBackButtonClick : CreateSessionUiEvent()
        data class InputTitle(val title: String) : CreateSessionUiEvent()
        object OnSessionTypeButtonClick : CreateSessionUiEvent()
        data class OnSessionTypeClick(val type: NeedToAttendType) : CreateSessionUiEvent()
        object OnDialogDismissRequest : CreateSessionUiEvent()
        object OnDateButtonClick : CreateSessionUiEvent()
        data class OnDateWriterConfirmClick(val date: String) : CreateSessionUiEvent()
        data class InputDescription(val description: String) : CreateSessionUiEvent()
        object OnCreateButtonClick : CreateSessionUiEvent()
    }
}
