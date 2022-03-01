package com.yapp.presentation.model

import com.yapp.domain.model.TeamEntity

typealias MemberId = Long
data class Team(
    val platform: String,
    val teamNumber: String,
    val members: List<MemberId>
) {
    fun TeamEntity.mapTo(): Team {
        return Team(
            platform = platform,
            teamNumber = teamNumber,
            members = members
        )
    }

}