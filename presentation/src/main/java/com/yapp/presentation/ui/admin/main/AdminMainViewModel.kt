package com.yapp.presentation.ui.admin.main

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetSessionListUseCase
import com.yapp.domain.usecases.GetUpcomingSessionUseCase
import com.yapp.presentation.model.Session.Companion.mapTo
import com.yapp.presentation.ui.admin.main.AdminMainContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminMainViewModel @Inject constructor(
    private val getSessionListUseCase: GetSessionListUseCase,
    private val getUpcomingSessionUseCase: GetUpcomingSessionUseCase
) : BaseViewModel<AdminMainUiState, AdminMainUiSideEffect, AdminMainUiEvent>(
    AdminMainUiState()
) {

    init {
        viewModelScope.launch {
            setState { copy(loadState = AdminMainUiState.LoadState.Loading) }
            getUpcomingSession()
            getSessions()
        }
    }

    override suspend fun handleEvent(event: AdminMainUiEvent) {
        when(event) {

        }
    }

    private suspend fun getUpcomingSession() {
        getUpcomingSessionUseCase()
            .collectWithCallback(
                onSuccess = { entity ->
                    if (entity != null) {
                        val session = entity.mapTo()
                        setState { copy(loadState = AdminMainUiState.LoadState.Idle, upcomingSession = session) }
                    } else {
                        setState { copy(loadState = AdminMainUiState.LoadState.Error) }
                    }
                },
                onFailed = {
                    setState { copy(loadState = AdminMainUiState.LoadState.Error) }
                }
            )
    }

    private suspend fun getSessions() {
        getSessionListUseCase()
            .collectWithCallback(
                onSuccess = { entities ->
                    val sessions = entities.map { entity -> entity.mapTo() }
                    setState { copy(loadState = AdminMainUiState.LoadState.Idle, sessions = sessions) }
                },
                onFailed = {
                    setState { copy(loadState = AdminMainUiState.LoadState.Error) }
                }
            )
    }
}
