package com.yapp.presentation.ui.admin.main

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetSessionListUseCase
import com.yapp.domain.util.DateUtil
import com.yapp.presentation.model.Session.Companion.mapTo
import com.yapp.presentation.model.collections.AttendanceList
import com.yapp.presentation.ui.admin.main.AdminMainContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminMainViewModel @Inject constructor(
    private val getSessionListUseCase: GetSessionListUseCase,
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
            .collectWithCallback(
                onSuccess = { entities ->
                    val sessions = entities.map { entity -> entity.mapTo() }
                    val upcomingSession =
                        sessions.firstOrNull { DateUtil.isUpcomingSession(it.date) }
                    val lastSessionId = when {
                        upcomingSession == null -> AttendanceList.DEFAULT_UPCOMING_SESSION_ID
                        upcomingSession.sessionId == 0 -> AttendanceList.DEFAULT_UPCOMING_SESSION_ID
                        DateUtil.isPastSession(upcomingSession.date) -> upcomingSession.sessionId
                        else -> upcomingSession.sessionId - 1
                    }

                    setState {
                        copy(
                            loadState = AdminMainUiState.LoadState.Idle,
                            upcomingSession = upcomingSession,
                            lastSessionId = lastSessionId,
                            sessions = sessions
                        )
                    }
                },
                onFailed = {
                    setState { copy(loadState = AdminMainUiState.LoadState.Error) }
                }
            )
    }
}
