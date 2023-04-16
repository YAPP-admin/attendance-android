package com.yapp.presentation.ui.member.score.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetMemberAttendanceListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SessionDetailViewModel @Inject constructor(
    private val getMemberAttendanceListUseCase: GetMemberAttendanceListUseCase,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<SessionDetailContract.SessionDetailUiState, SessionDetailContract.SessionDetailUiSideEffect, SessionDetailContract.SessionDetailUiEvent>(
    SessionDetailContract.SessionDetailUiState()
) {

    init {
        setState { copy(loadState = SessionDetailContract.SessionDetailUiState.LoadState.Loading) }
        val sessionId = savedStateHandle.get<Int>("session")

        if (sessionId != null) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    getMemberAttendanceListUseCase().collect { result ->
                        result.onSuccess { (sessions, attendances) ->
                            if (attendances.isEmpty()) {
                                setState { copy(loadState = SessionDetailContract.SessionDetailUiState.LoadState.Error) }
                                return@onSuccess
                            }

                            setState {
                                copy(
                                    loadState = SessionDetailContract.SessionDetailUiState.LoadState.Idle,
                                    session = sessions[sessionId] to attendances[sessionId]
                                )
                            }
                        }
                            .onFailure {
                                setState { copy(loadState = SessionDetailContract.SessionDetailUiState.LoadState.Error) }
                            }
                    }
                }
            }
        } else {
            setState { copy(loadState = SessionDetailContract.SessionDetailUiState.LoadState.Error) }
        }
    }

    override suspend fun handleEvent(event: SessionDetailContract.SessionDetailUiEvent) {
        TODO("Not yet implemented")
    }

}