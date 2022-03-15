package com.yapp.presentation.ui.admin.main

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetSessionListUseCase
import com.yapp.presentation.model.Session.Companion.mapTo
import com.yapp.presentation.ui.admin.main.AdminMainContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
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
            getSessionListUseCase().collectWithCallback(
                onSuccess = { entities ->
                    val sessions = entities.map { entity -> entity.mapTo() }
                    setState { copy(isLoading = false, sessions = sessions) }
                },
                onFailed = {
                    // TODO 에러핸들링 필요합니다.
                }
            )
        }
    }

    override fun setEvent(event: AdminMainUiEvent) {
        super.setEvent(event)
    }

    override fun handleEvent(event: AdminMainUiEvent) {
        TODO("Not yet implemented")
    }
}
