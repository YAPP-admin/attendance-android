package com.yapp.data.model

import com.yapp.domain.model.ConfigEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class ConfigModel(
    val generation: Int?,
    @SerialName("session_count")
    val sessionCount: Int?,
    @SerialName("admin_password")
    val adminPassword: String?
) {
    companion object {
        fun ConfigModel.mapToEntity(): ConfigEntity {
            return ConfigEntity(
                generation = generation ?: -1,
                sessionCount = sessionCount ?: -1,
                adminPassword = adminPassword ?: ""
            )
        }
    }
}