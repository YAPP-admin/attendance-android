package com.yapp.data.model

import com.yapp.domain.model.ConfigEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ConfigModel(
    val generation: Int? = null,
    @SerialName("session_count")
    val sessionCount: Int? = null,
    @SerialName("admin_password")
    val adminPassword: String? = null
) {
    companion object {
        fun ConfigModel.mapToEntity(): ConfigEntity {
            return ConfigEntity(
                generation = generation!!,
                sessionCount = sessionCount!!,
                adminPassword = adminPassword!!
            )
        }
    }
}