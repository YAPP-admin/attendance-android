package com.yapp.data.model

import com.yapp.domain.model.TeamEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class TeamModel(
    @SerialName("team")
    val type: String? = null,
    @SerialName("count")
    val number: Int? = null
) {
    companion object {
        fun TeamModel.mapToEntity(): TeamEntity {
            return TeamEntity(
                type = type!!,
                number = number!!,
            )
        }
    }
}