package com.yapp.presentation.ui.member.todaysession

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetUpcomingSessionUseCase
import com.yapp.presentation.model.Session.Companion.mapTo
import com.yapp.presentation.ui.member.todaysession.TodaySessionContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodaySessionViewModel @Inject constructor(
    private val getUpcomingSessionUseCase: GetUpcomingSessionUseCase,
) : BaseViewModel<TodaySessionUiState, TodaySessionUiSideEffect, TodaySessionUiEvent>(
    TodaySessionUiState()
) {
    init {
        viewModelScope.launch {
            getUpcomingSessionUseCase()
                .collectWithCallback(
                    onSuccess = { entity ->
                        val session = entity?.mapTo()
                        setState { copy(isLoading = false, todaySession = session) }
                    },
                    onFailed = {
                        // TODO 엠티뷰 보여주기
                    }
                )
        }
    }

    override fun setEvent(event: TodaySessionUiEvent) {
        super.setEvent(event)
    }

    override fun handleEvent(event: TodaySessionUiEvent) {
        TODO("Not yet implemented")
    }
}