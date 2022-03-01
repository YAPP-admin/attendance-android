package com.yapp.data.model

import com.yapp.domain.model.TeamEntity


typealias MemberId = Long

internal class TeamModel(
    val platform: String?,
    val teamNumber: String?,
    val members: List<MemberId>?
) {
    companion object {
        fun TeamModel.mapToEntity(): TeamEntity {
            return TeamEntity(
                platform = platform ?: "",
                teamNumber = teamNumber ?: "",
                members = members ?: emptyList()
            )
        }
    }
}