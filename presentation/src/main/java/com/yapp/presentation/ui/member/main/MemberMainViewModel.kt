package com.yapp.presentation.ui.member.main

import androidx.lifecycle.viewModelScope
import com.yapp.common.base.BaseViewModel
import com.yapp.domain.model.types.TeamType
import com.yapp.domain.usecases.GetCurrentMemberInfoUseCase
import com.yapp.presentation.ui.member.main.MemberMainContract.MemberMainUiEvent
import com.yapp.presentation.ui.member.main.MemberMainContract.MemberMainUiSideEffect
import com.yapp.presentation.ui.member.main.MemberMainContract.MemberMainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberMainViewModel @Inject constructor(
    getMemberGetCurrentMemberInfoUseCase: GetCurrentMemberInfoUseCase
) :
    BaseViewModel<MemberMainUiState, MemberMainUiSideEffect, MemberMainUiEvent>(MemberMainUiState()) {

    init {
        viewModelScope.launch {
            val member = getMemberGetCurrentMemberInfoUseCase.invoke().getOrNull()

            if(member != null) {
                setState { copy(isShowTeamDialog = member.team.type.value == TeamType.NONE.value) }
            }
        }
    }

    override suspend fun handleEvent(event: MemberMainUiEvent) {
        when (event) {
            is MemberMainUiEvent.OnClickBottomNavigationTab -> {
                setState { copy(selectedTab = event.tab) }
                setEffect( MemberMainUiSideEffect.NavigateToRoute(event.tab))
            }
            is MemberMainUiEvent.OnNextTime -> {
                setState { copy(isShowTeamDialog = false) }
            }
            is MemberMainUiEvent.OnSelectTeamScreen -> {
                setState { copy(isShowTeamDialog = false) }
                setEffect(MemberMainUiSideEffect.NavigateToTeam)
            }

        }
    }
}

