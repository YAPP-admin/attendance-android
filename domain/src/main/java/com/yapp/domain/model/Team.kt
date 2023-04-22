package com.yapp.domain.model

import com.yapp.domain.model.types.TeamType


data class Team(
    val type: TeamType,
    val number: Int,
) {
    override fun toString(): String {
        return "${type.value} $number"
    }

    companion object {
        fun empty() = Team(TeamType.NONE, 0)
    }
}