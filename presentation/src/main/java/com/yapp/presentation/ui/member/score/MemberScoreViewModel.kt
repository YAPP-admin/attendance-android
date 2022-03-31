package com.yapp.presentation.ui.member.score

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetSessionListUseCase
import com.yapp.presentation.model.Session.Companion.mapTo
import com.yapp.presentation.ui.member.score.MemberScoreContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberScoreViewModel @Inject constructor(
    private val getSessionListUseCase: GetSessionListUseCase
) : BaseViewModel<MemberScoreUiState, MemberScoreUiSideEffect, MemberScoreUiEvent>(
    MemberScoreUiState()
) {
    init {
        viewModelScope.launch {
            getSessionListUseCase()
                .collectWithCallback(
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

    override fun setEvent(event: MemberScoreUiEvent) {
        super.setEvent(event)
    }

    override fun handleEvent(event: MemberScoreUiEvent) {
        TODO("Not yet implemented")
    }
}