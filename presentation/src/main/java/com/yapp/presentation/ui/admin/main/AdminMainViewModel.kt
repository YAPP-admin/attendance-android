package com.yapp.presentation.ui.admin.main

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetSessionListUseCase
import com.yapp.domain.usecases.GetUpcomingSessionUseCase
import com.yapp.domain.util.DateUtil
import com.yapp.presentation.model.Session.Companion.mapTo
import com.yapp.presentation.ui.admin.main.AdminMainContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminMainViewModel @Inject constructor(
    private val getSessionListUseCase: GetSessionListUseCase
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
        when(event) {

        }
    }

    private suspend fun getSessions() {
        getSessionListUseCase()
            .collectWithCallback(
                onSuccess = { entities ->
                    val sessions = entities.map { entity -> entity.mapTo() }
                    val upcomingSession = sessions.firstOrNull { DateUtil.isUpcomingSession(it.date) }
                    setState {
                        copy(
                            loadState = AdminMainUiState.LoadState.Idle,
                            upcomingSession = upcomingSession,
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
