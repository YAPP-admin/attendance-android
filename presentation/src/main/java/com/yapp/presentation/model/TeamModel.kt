package com.yapp.presentation.model

import com.google.gson.annotations.SerializedName

data class TeamModel(
    @SerializedName("team")
    val team: String = "",
    val count: Int = 0
)