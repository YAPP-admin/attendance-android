package com.yapp.data.model

import com.yapp.domain.model.TeamEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class TeamModel(
    val type: String? = null,
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