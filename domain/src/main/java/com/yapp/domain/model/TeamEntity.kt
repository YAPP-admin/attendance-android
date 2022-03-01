package com.yapp.domain.model


typealias MemberId = Long
class TeamEntity(
    val platform: String,
    val teamNumber: String,
    val members: List<MemberId>
)