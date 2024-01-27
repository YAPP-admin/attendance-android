package com.yapp.presentation.ui.member.score

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.usecases.GetMemberAttendanceListUseCase
import com.yapp.domain.util.DateUtil
import com.yapp.presentation.ui.member.score.MemberScoreContract.MemberScoreUiEvent
import com.yapp.presentation.ui.member.score.MemberScoreContract.MemberScoreUiSideEffect
import com.yapp.presentation.ui.member.score.MemberScoreContract.MemberScoreUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberScoreViewModel @Inject constructor(
    private val getMemberAttendanceListUseCase: GetMemberAttendanceListUseCase,
    private val dateUtil: DateUtil
) : BaseViewModel<MemberScoreUiState, MemberScoreUiSideEffect, MemberScoreUiEvent>(initialState = MemberScoreUiState()) {

    init {
        viewModelScope.launch { fetchMemberScore() }
    }

    private suspend fun fetchMemberScore() {
        setState { copy(loadState = MemberScoreUiState.LoadState.Loading) }
        getMemberAttendanceListUseCase().collectLatest { result ->
            result.onSuccess { (sessions, attendances) ->
                if (attendances.isEmpty()) {
                    setState { copy(loadState = MemberScoreUiState.LoadState.Error) }
                    return@onSuccess
                }

                val attendanceList = sessions zip attendances
                setState {
                    copy(
                        loadState = MemberScoreUiState.LoadState.Idle,
                        attendanceList = attendanceList,
                        lastAttendanceList = attendanceList.filter { (session, _) ->
                            with(dateUtil) { currentTime isAfterFrom session.startTime }
                        }
                    )
                }
            }.onFailure {
                setState { copy(loadState = MemberScoreUiState.LoadState.Error) }
            }
        }
    }

    override suspend fun handleEvent(event: MemberScoreUiEvent) {
        when (event) {
            is MemberScoreUiEvent.GetMemberScore -> fetchMemberScore()
        }
    }
}
