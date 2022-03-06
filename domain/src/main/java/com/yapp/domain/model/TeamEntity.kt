package com.yapp.domain.model

import com.yapp.domain.model.types.PlatformTypeEntity


typealias MemberId = Long
data class TeamEntity(
    val platform: PlatformTypeEntity,
    val teamNumber: String
)