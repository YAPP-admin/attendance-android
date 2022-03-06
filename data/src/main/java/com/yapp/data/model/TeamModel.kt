package com.yapp.data.model

import com.yapp.data.model.types.PlatformTypeModel
import com.yapp.data.model.types.PlatformTypeModel.Companion.mapToEntity
import com.yapp.domain.model.TeamEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


typealias MemberId = Long

@Serializable
data class TeamModel(
    val platform: PlatformTypeModel? = null,
    @SerialName("serial_name")
    val teamNumber: String? = null
) {
    companion object {
        fun TeamModel.mapToEntity(): TeamEntity {
            return TeamEntity(
                platform = platform!!.mapToEntity(),
                teamNumber = teamNumber!!,
            )
        }
    }
}