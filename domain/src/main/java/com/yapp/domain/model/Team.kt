package com.yapp.domain.model

import com.yapp.domain.model.types.TeamType


data class Team(
    val type: TeamType,
    val number: Int
)