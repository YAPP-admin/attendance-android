package com.yapp.presentation.ui.admin.createsession

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.SetSessionUseCase
import com.yapp.presentation.ui.admin.createsession.CreateSessionContract.CreateSessionUiEvent
import com.yapp.presentation.ui.admin.createsession.CreateSessionContract.CreateSessionUiSideEffect
import com.yapp.presentation.ui.admin.createsession.CreateSessionContract.CreateSessionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

@HiltViewModel
class CreateSessionViewModel @Inject constructor(
    private val setSessionUseCase: SetSessionUseCase,
) : BaseViewModel<CreateSessionUiState, CreateSessionUiSideEffect, CreateSessionUiEvent>(
    CreateSessionUiState()
) {
    init {
        setState {
            copy(
                code = Random.nextInt(RANDOM_CODE_RANGE).toString()
            )
        }
    }

    override suspend fun handleEvent(event: CreateSessionUiEvent) {
        when (event) {
            is CreateSessionUiEvent.OnScreenClick ->
                setEffect(CreateSessionUiSideEffect.KeyboardHide)

            is CreateSessionUiEvent.OnBackButtonClick ->
                setEffect(CreateSessionUiSideEffect.NavigateToPreviousScreen)

            is CreateSessionUiEvent.InputTitle -> setState { copy(title = event.title) }

            is CreateSessionUiEvent.OnSessionTypeButtonClick -> {
                viewModelScope.launch {
                    setEffect(CreateSessionUiSideEffect.KeyboardHide)
                    delay(300) // 키보드를 완전히 숨긴 후 바텀시트를 출력해야 함
                    setEffect(CreateSessionUiSideEffect.ShowBottomSheet)
                }
            }

            is CreateSessionUiEvent.OnSessionTypeClick -> {
                setState { copy(type = event.type) }
                setEffect(CreateSessionUiSideEffect.HideBottomSheet)
            }

            is CreateSessionUiEvent.OnDialogDismissRequest -> setState { copy(showDialog = false) }

            is CreateSessionUiEvent.OnDateButtonClick ->
                setState { copy(showDialog = true) }

            is CreateSessionUiEvent.OnDateWriterConfirmClick -> {
                setState {
                    copy(
                        showDialog = false,
                        startTime = event.date
                    )
                }
            }

            is CreateSessionUiEvent.InputDescription ->
                setState { copy(description = event.description) }

            is CreateSessionUiEvent.OnCreateButtonClick -> {
                if (currentState.enableCreate)
                    setSession()
            }

        }
    }

    private suspend fun setSession() {
        setState { copy(loadState = CreateSessionUiState.LoadState.Loading) }

        currentState.type?.let { type ->
            setSessionUseCase(
                title = currentState.title,
                type = type,
                startTime = currentState.startTime,
                description = currentState.description,
                code = currentState.code,
            ).onSuccess {
                setEffect(CreateSessionUiSideEffect.ShowToast("세션 생성이 완료되었습니다."))
                setEffect(CreateSessionUiSideEffect.NavigateToPreviousScreen)
            }.onFailure {
                setEffect(CreateSessionUiSideEffect.ShowToast("알 수 없는 오류가 발생했습니다.\n다시 시도해주세요."))
            }.also {
                setState { copy(loadState = CreateSessionUiState.LoadState.Idle) }
            }
        } ?: run {
            setState { copy(loadState = CreateSessionUiState.LoadState.Idle) }
            setEffect(CreateSessionUiSideEffect.ShowToast("세션 타입을 선택해주세요."))
        }
    }

    companion object {
        private val RANDOM_CODE_RANGE = 1000..9999
    }
}
