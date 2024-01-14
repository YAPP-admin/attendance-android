package com.yapp.presentation.ui.admin.main

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.model.Session
import com.yapp.domain.model.collections.AttendanceList
import com.yapp.domain.usecases.GetSessionListUseCase
import com.yapp.domain.usecases.GetUpcomingSessionUseCase
import com.yapp.domain.util.RenewDateUtil
import com.yapp.presentation.ui.admin.main.AdminMainContract.AdminMainUiEvent
import com.yapp.presentation.ui.admin.main.AdminMainContract.AdminMainUiSideEffect
import com.yapp.presentation.ui.admin.main.AdminMainContract.AdminMainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminMainViewModel @Inject constructor(
    private val getSessionListUseCase: GetSessionListUseCase,
    private val getUpcomingSessionUseCase: GetUpcomingSessionUseCase,
    private val dateUtil: RenewDateUtil
) : BaseViewModel<AdminMainUiState, AdminMainUiSideEffect, AdminMainUiEvent>(
    AdminMainUiState()
) {

    init {
        viewModelScope.launch {
            setState { copy(loadState = AdminMainUiState.LoadState.Loading) }
            getSessions()
        }
    }

    override suspend fun handleEvent(event: AdminMainUiEvent) {
        when (event) {
            is AdminMainUiEvent.OnUserScoreCardClicked -> setEffect(
                AdminMainUiSideEffect.NavigateToAdminTotalScore(event.lastSessionId)
            )
            is AdminMainUiEvent.OnCreateSessionClicked -> setEffect(
                AdminMainUiSideEffect.NavigateToCreateSession
            )
            is AdminMainUiEvent.OnSessionClicked -> setEffect(
                AdminMainUiSideEffect.NavigateToManagement(event.sessionId, event.sessionTitle)
            )
            is AdminMainUiEvent.OnLogoutClicked -> setEffect(
                AdminMainUiSideEffect.NavigateToLogin
            )
        }
    }

    private suspend fun getSessions() {
        getSessionListUseCase()
            .onSuccess { sessions ->
                val upcomingSession = getUpcomingSessionUseCase().getOrThrow()

                upcomingSession?.let {
                    var lastSessionId = if (dateUtil.isPastDate(upcomingSession.date)) it.sessionId else it.sessionId - 1
                    if (lastSessionId < 0) lastSessionId = AttendanceList.DEFAULT_UPCOMING_SESSION_ID
                    setState { copy(lastSessionId = lastSessionId) }
                }

                setState {
                    copy(
                        loadState = AdminMainUiState.LoadState.Idle,
                        upcomingSession = upcomingSession,
                        sessions = sessions
                    )
                }
            }
            .onFailure {
                setState { copy(loadState = AdminMainUiState.LoadState.Error) }
            }
    }
}
