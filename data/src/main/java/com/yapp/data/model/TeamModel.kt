package com.yapp.data.model

import com.google.firebase.firestore.PropertyName
import com.yapp.domain.model.TeamEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class TeamModel(
    @PropertyName("type")
    val type: String? = null,
    @PropertyName("number")
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