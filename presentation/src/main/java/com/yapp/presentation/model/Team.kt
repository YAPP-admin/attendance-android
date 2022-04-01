package com.yapp.presentation.model

import com.yapp.domain.model.TeamEntity
import com.yapp.presentation.model.type.TeamType

data class Team(
    val type: TeamType? = null,
    val number: Int? = null,
) {

    companion object {
        fun TeamEntity.mapTo(): Team {
            return Team(
                type = TeamType.of(type),
                number = number
            )
        }
    }

}