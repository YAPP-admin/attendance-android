package com.yapp.data.model


typealias MemberId = Long
internal class TeamModel(
    val platform: String?,
    val teamNumber: String,
    val members: List<MemberId>?
)