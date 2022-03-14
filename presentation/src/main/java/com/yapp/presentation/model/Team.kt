package com.yapp.presentation.model

import com.yapp.domain.model.TeamEntity
import com.yapp.presentation.model.type.PlatformType

data class Team(
    val platform: PlatformType? = null,
    val number: Int? = null,
) {

    companion object {
        fun TeamEntity.mapTo(): Team {
            return Team(
                platform = PlatformType.valueOf(platform.name),
                number = number
            )
        }
    }

}