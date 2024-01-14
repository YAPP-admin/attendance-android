package com.yapp.data.model

import com.google.firebase.firestore.PropertyName
import com.yapp.domain.model.Team
import com.yapp.domain.model.types.TeamType
import kotlinx.serialization.Serializable


@Serializable
data class TeamEntity(
    @PropertyName("type")
    val type: String? = null,
    @PropertyName("number")
    val number: Int? = null,
)

fun TeamEntity.toDomain(): Team {
    return Team(
        type = TeamType.from(type!!),
        number = number!!,
    )
}

fun Team.toData(): TeamEntity {
    return TeamEntity(
        type = type.name,
        number = number
    )
}
