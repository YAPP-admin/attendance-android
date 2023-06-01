package com.yapp.presentation.ui.admin.totalscore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.model.Member
import com.yapp.domain.model.collections.AttendanceList
import com.yapp.domain.usecases.GetAllMemberUseCase
import com.yapp.presentation.ui.admin.AdminConstants.KEY_LAST_SESSION_ID
import com.yapp.presentation.ui.admin.totalscore.AdminTotalScoreContract.AdminTotalScoreUiEvent
import com.yapp.presentation.ui.admin.totalscore.AdminTotalScoreContract.AdminTotalScoreUiSideEffect
import com.yapp.presentation.ui.admin.totalscore.AdminTotalScoreContract.AdminTotalScoreUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminTotalScoreViewModel @Inject constructor(
    private val getAllMemberUseCase: GetAllMemberUseCase,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<AdminTotalScoreUiState, AdminTotalScoreUiSideEffect, AdminTotalScoreUiEvent>(
    AdminTotalScoreUiState()
) {
    init {
        viewModelScope.launch {
            updateLoadState(AdminTotalScoreUiState.LoadState.Loading)
            setState {
                copy(
                    lastSessionId = savedStateHandle.get<Int>(KEY_LAST_SESSION_ID)
                        ?: AttendanceList.DEFAULT_UPCOMING_SESSION_ID
                )
            }
            getAllScoresGroupByTeam()
        }
    }

    override suspend fun handleEvent(event: AdminTotalScoreUiEvent) {
        when (event) {
            is AdminTotalScoreUiEvent.OnBackArrowClick -> setEffect(AdminTotalScoreUiSideEffect.NavigateToPreviousScreen)
            is AdminTotalScoreUiEvent.OnSectionTypeChange -> {
                getScoreBySessionType(event.sectionType)
            }
        }
    }

    private suspend fun getScoreBySessionType(sectionType: AdminTotalScoreUiState.SectionType) {
        setState { copy(sectionType = sectionType) }
        when (sectionType) {
            AdminTotalScoreUiState.SectionType.Team -> getAllScoresGroupByTeam()
            AdminTotalScoreUiState.SectionType.Position -> getAllScoresGroupByPosition()
        }
    }

    private suspend fun getAllScoresGroupByTeam() {
        getAllScore(
            groupKey = { it.team },
            sectionNameFunction = { "${it.type.value} ${it.number}팀" }
        )
    }

    private suspend fun getAllScoresGroupByPosition() {
        getAllScore(
            groupKey = { it.position },
            sectionNameFunction = { it.value }
        )
    }

    private suspend fun <T> getAllScore(
        groupKey: (Member) -> T,
        sectionNameFunction: (T) -> String,
    ) {
        updateLoadState(AdminTotalScoreUiState.LoadState.Loading)
        getAllMemberUseCase()
            .catch { setState { copy(loadState = AdminTotalScoreUiState.LoadState.Error) } }
            .collectLatest { members ->
                val memberByGroup = members.groupBy(groupKey)
                val sectionItemStates = getSectionItemStates(
                    memberBySection = memberByGroup,
                    getSectionName = sectionNameFunction
                )
                setState {
                    copy(
                        loadState = AdminTotalScoreUiState.LoadState.Idle,
                        sectionItemStates = sectionItemStates.toImmutableList()
                    )
                }
        }
    }

    private fun <T> getSectionItemStates(
        memberBySection: Map<T, List<Member>>,
        getSectionName: (T) -> String,
    ): List<AdminTotalScoreUiState.SectionItemState> {
        val lastSessionId = currentState.lastSessionId
        return memberBySection.map { section ->
            AdminTotalScoreUiState.SectionItemState(
                sectionName = getSectionName(section.key),
                members = section.value.sortedWith(
                    compareBy<Member> { it.attendances.getTotalAttendanceScore(lastSessionId) }
                        .thenBy { it.name }
                ).map { member ->
                    AdminTotalScoreUiState.MemberState(
                        member.name,
                        member.attendances.getTotalAttendanceScore(lastSessionId)
                    )
                }.toImmutableList(),
            )
        }.sortedBy { it.sectionName }
    }

    private fun updateLoadState(state: AdminTotalScoreUiState.LoadState) {
        setState { copy(loadState = state) }
    }
}
