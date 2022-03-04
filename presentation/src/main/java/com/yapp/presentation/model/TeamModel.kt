package com.yapp.presentation.model

import com.google.gson.annotations.SerializedName

data class TeamModel(
    @SerializedName("team")
    val teamType: String = "",
    @SerializedName("count")
    val teamNum: Int = -1
)