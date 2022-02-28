package com.yapp.domain.model


typealias MemberId = Long
internal class TeamEntity(
    val platform: String?,
    val teamNumber: String,
    val members: List<MemberId>?
)