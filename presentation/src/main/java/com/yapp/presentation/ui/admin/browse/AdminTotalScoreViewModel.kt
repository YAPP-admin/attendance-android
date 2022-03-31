package com.yapp.presentation.ui.admin.browse

import com.yapp.common.base.BaseViewModel
import com.yapp.presentation.ui.admin.browse.AdminTotalScoreContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminTotalScoreViewModel @Inject constructor(

) : BaseViewModel<AdminTotalScoreUiState, AdminTotalScoreUiSideEffect, AdminTotalScoreUiEvent>(
    AdminTotalScoreUiState()
) {
    init {
        getAllScores()
    }

    override fun handleEvent(event: AdminTotalScoreUiEvent) {
        when (event) {

        }
    }

    private fun getAllScores() {
        // todo: DB에서 출결 정보 가져온 뒤 최종 점수 계산 & state 변경
        val member1 = MemberScore("신짱구", 100)
        val member2 = MemberScore("노진구", 80)
        val member3 = MemberScore("흰둥이", 90)
        val member4 = MemberScore("도라에몽", 65)
        val allRounderTeam = listOf(member1, member2, member3, member4)
        val webTeam = listOf(member1, member2, member3, member4)
        val androidTeam = listOf(member1, member2, member3, member4)
        val iosTeam = listOf(member1, member2, member3, member4)
        val sample = mapOf(
            Pair("All-rounder 1팀", allRounderTeam),
            Pair("Web 1팀", webTeam),
            Pair("Android 1팀", androidTeam),
            Pair("iOS 1팀", iosTeam)
        )
        setState {
            copy(teams = sample)
        }
    }
}