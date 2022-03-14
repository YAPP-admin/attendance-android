package com.yapp.data.model

import com.yapp.data.model.types.PlatformTypeModel
import com.yapp.data.model.types.PlatformTypeModel.Companion.mapToEntity
import com.yapp.domain.model.TeamEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


typealias MemberId = Long

@Serializable
data class TeamModel(
    @SerialName("team")
    val platform: PlatformTypeModel? = null,
    val number: Int? = null
) {
    companion object {
        fun TeamModel.mapToEntity(): TeamEntity {
            return TeamEntity(
                platform = platform!!.mapToEntity(),
                number = number!!,
            )
        }
    }
}