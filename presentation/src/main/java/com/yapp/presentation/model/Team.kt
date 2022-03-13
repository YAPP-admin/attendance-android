package com.yapp.presentation.model

import com.yapp.domain.model.TeamEntity
import com.yapp.presentation.model.type.PlatformType

typealias MemberId = Long

data class Team(
    val platform: PlatformType,
    val teamNumber: String,
) {

    companion object {
        fun TeamEntity.mapTo(): Team {
            return Team(
                platform = PlatformType.valueOf(platform.name),
                teamNumber = teamNumber
            )
        }
    }

}