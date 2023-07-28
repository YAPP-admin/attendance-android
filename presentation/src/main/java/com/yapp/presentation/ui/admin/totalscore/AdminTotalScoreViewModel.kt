package com.yapp.presentation.ui.admin.totalscore

import FoldableHeaderItemState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.model.Member
import com.yapp.domain.model.Team
import com.yapp.domain.model.collections.AttendanceList
import com.yapp.domain.model.types.PositionType
import com.yapp.domain.usecases.GetAllMemberUseCase
import com.yapp.presentation.ui.admin.AdminConstants
import com.yapp.presentation.ui.admin.management.components.foldableItem.FoldableItemState
import com.yapp.presentation.ui.admin.management.components.foldableItem.PositionItemWithButtonContentState
import com.yapp.presentation.ui.admin.management.components.foldableItem.PositionItemWithScoreContentState
import com.yapp.presentation.ui.admin.management.components.foldableItem.TeamItemWithButtonContentState
import com.yapp.presentation.ui.admin.management.components.foldableItem.TeamItemWithScoreContentState
import com.yapp.presentation.ui.admin.management.components.foldableItem.foldableContentItem.FoldableContentItemWithScoreState
import com.yapp.presentation.ui.admin.management.dto.ManagementTabLayoutState
import com.yapp.presentation.ui.admin.totalscore.AdminTotalScoreContract.AdminTotalScoreUiEvent
import com.yapp.presentation.ui.admin.totalscore.AdminTotalScoreContract.AdminTotalScoreUiSideEffect
import com.yapp.presentation.ui.admin.totalscore.AdminTotalScoreContract.AdminTotalScoreUiState
import com.yapp.presentation.ui.admin.totalscore.dto.AdminTotalScoreSharedData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminTotalScoreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getAllMemberUseCase: GetAllMemberUseCase
) : BaseViewModel<AdminTotalScoreUiState, AdminTotalScoreUiSideEffect, AdminTotalScoreUiEvent>(AdminTotalScoreUiState()) {

    companion object {
        const val SCORE_LIMIT = 70
    }

    private val members: StateFlow<List<Member>> = getAllMemberUseCase()
        .catch { setState { this.copy(loadState = AdminTotalScoreUiState.LoadState.Error) } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000L),
            initialValue = emptyList()
        )

    private val tabIndex: MutableStateFlow<Int> = MutableStateFlow(uiState.value.tabLayoutState.selectedIndex)

    init {
        val upComingSessionId = savedStateHandle.get<Int>(AdminConstants.KEY_LAST_SESSION_ID) ?: AttendanceList.DEFAULT_UPCOMING_SESSION_ID

        initializeState(upComingSessionId)

        /**
         * members변경 이외에도 tabIndex가 변경 될 시 UiState를 Update해준다.
         */
        viewModelScope.launch {
            members.combine(tabIndex, transform = { members, tabIndex ->
                members to tabIndex
            }).collect { (members, tabIndex) ->
                setState { this.copy(foldableItemStates = members.toFoldableItemGroup(upComingSessionId, tabIndex)) }
            }
        }
    }

    override suspend fun handleEvent(event: AdminTotalScoreUiEvent) {
        when (event) {
            is AdminTotalScoreUiEvent.OnBackArrowClick -> {
                setEffect(AdminTotalScoreUiSideEffect.NavigateToPreviousScreen)
            }

            is AdminTotalScoreUiEvent.OnTabItemSelected -> {
                setState {
                    tabIndex.value = event.tabIndex
                    this.copy(tabLayoutState = tabLayoutState.select(event.tabIndex))
                }
            }

            is AdminTotalScoreUiEvent.OnPositionTypeHeaderItemClicked -> {
                setState {
                    this.copy(
                        foldableItemStates = currentState.foldableItemStates
                            .filterIsInstance<PositionItemWithScoreContentState>()
                            .map { itemState ->
                                if (itemState.position == event.positionName) {
                                    return@map itemState.setHeaderItemExpandable(isExpand = itemState.headerItem.isExpanded.not())
                                }

                                itemState
                            }
                            .toImmutableList()
                    )
                }
            }

            is AdminTotalScoreUiEvent.OnTeamTypeHeaderItemClicked -> {
                setState {
                    this.copy(
                        foldableItemStates = currentState.foldableItemStates
                            .filterIsInstance<TeamItemWithScoreContentState>()
                            .map { itemState ->
                                if (itemState.teamName == event.teamName && itemState.teamNumber == event.teamNumber) {
                                    return@map itemState.setHeaderItemExpandable(isExpand = itemState.headerItem.isExpanded.not())
                                }

                                itemState
                            }
                            .toImmutableList()
                    )
                }
            }
        }
    }

    private fun initializeState(sessionId: Int) {
        setState {
            this.copy(
                loadState = AdminTotalScoreUiState.LoadState.Idle,
                shared = AdminTotalScoreSharedData(sessionId = sessionId),
                tabLayoutState = ManagementTabLayoutState.init(),
            )
        }
    }

    private fun List<Member>.toFoldableItemGroup(upComingSessionId: Int, tabIndex: Int): ImmutableList<FoldableItemState> {
        return when (tabIndex) {
            ManagementTabLayoutState.INDEX_TEAM -> {
                this.groupBy { it.team }
                    .entries.sortedWith(comparator = compareBy({ it.key.type }, { it.key.number }))
                    .map { (team, members) ->
                        TeamItemWithScoreContentState(
                            headerItem = toTeamTypeHeaderItem(team = team, members = members),
                            contentItems = members
                                .map { member ->
                                    toTeamTypeContentItem(member = member, upComingSessionId = upComingSessionId)
                                }
                                .sortedBy { it.score }
                                .toImmutableList()
                        )
                    }
            }

            ManagementTabLayoutState.INDEX_POSITION -> {
                this.groupBy { it.position }
                    .entries.sortedBy { it.key.value }
                    .map { (position, members) ->
                        PositionItemWithScoreContentState(
                            headerItem = toPositionTypeHeaderItem(positionType = position, members = members),
                            contentItems = members
                                .map { member ->
                                    toPositionTypeContentItem(member = member, upComingSessionId = upComingSessionId)
                                }
                                .sortedBy { it.score }
                                .toImmutableList()
                        )
                    }
            }

            else -> error("${uiState.value.tabLayoutState.selectedIndex}는 잘못된 Tablayout Index입니다.")
        }.toImmutableList()
    }

    private fun toPositionTypeHeaderItem(positionType: PositionType, members: List<Member>): FoldableHeaderItemState.PositionType {
        return FoldableHeaderItemState.PositionType(
            position = positionType.value,
            isExpanded = currentState.foldableItemStates
                .filterIsInstance<PositionItemWithScoreContentState>()
                .find { it.position == positionType.value }?.headerItem?.isExpanded ?: false,
            attendMemberCount = null,
            allTeamMemberCount = members.size
        )
    }

    private fun toTeamTypeHeaderItem(team: Team, members: List<Member>): FoldableHeaderItemState.TeamType {
        return FoldableHeaderItemState.TeamType(
            teamName = team.type.value,
            teamNumber = team.number,
            isExpanded = currentState.foldableItemStates
                .filterIsInstance<TeamItemWithScoreContentState>()
                .find { it.teamName == team.type.value && it.teamNumber == team.number }?.headerItem?.isExpanded ?: false,
            attendMemberCount = null,
            allTeamMemberCount = members.size
        )
    }

    private fun toPositionTypeContentItem(member: Member, upComingSessionId: Int): FoldableContentItemWithScoreState.PositionType {
        val score = member.attendances.getTotalAttendanceScore(upComingSessionId)

        return FoldableContentItemWithScoreState.PositionType(
            memberId = member.id,
            score = score,
            shouldShowWarning = score < SCORE_LIMIT,
            memberName = member.name,
            teamType = member.team.type.value,
            teamNumber = member.team.number,
        )
    }

    private fun toTeamTypeContentItem(member: Member, upComingSessionId: Int): FoldableContentItemWithScoreState.TeamType {
        val score = member.attendances.getTotalAttendanceScore(upComingSessionId)

        return FoldableContentItemWithScoreState.TeamType(
            memberId = member.id,
            score = score,
            shouldShowWarning = score < SCORE_LIMIT,
            memberName = member.name,
            position = member.position.value
        )
    }

}

