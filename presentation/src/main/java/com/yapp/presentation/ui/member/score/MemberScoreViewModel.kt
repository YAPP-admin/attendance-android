package com.yapp.presentation.ui.member.score

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetMemberAttendanceListUseCase
import com.yapp.domain.util.DateUtil
import com.yapp.presentation.ui.member.score.MemberScoreContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberScoreViewModel @Inject constructor(
    private val getMemberAttendanceListUseCase: GetMemberAttendanceListUseCase,
) : BaseViewModel<MemberScoreUiState, MemberScoreUiSideEffect, MemberScoreUiEvent>(initialState = MemberScoreUiState()) {

    private suspend fun fetchMemberScore() = viewModelScope.launch {
        setState { copy(loadState = MemberScoreUiState.LoadState.Loading) }
        getMemberAttendanceListUseCase()
            .onSuccess { (sessions, attendances) ->
                if (attendances.isEmpty()) {
                    setState { copy(loadState = MemberScoreUiState.LoadState.Error) }
                    return@onSuccess
                }

                val attendanceList = sessions zip attendances
                setState {
                    copy(
                        loadState = MemberScoreUiState.LoadState.Idle,
                        attendanceList = attendanceList,
                        lastAttendanceList = attendanceList.filter {
                            DateUtil.isPastSession(it.first.date)
                        }
                    )
                }
            }.onFailure {
                setState { copy(loadState = MemberScoreUiState.LoadState.Error) }
            }
    }

    override suspend fun handleEvent(event: MemberScoreUiEvent) {
        when (event) {
            is MemberScoreUiEvent.GetMemberScore -> fetchMemberScore()
        }
    }
}