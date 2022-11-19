package com.yapp.data.model

import com.yapp.domain.model.Config
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ConfigEntity(
    val generation: Int? = null,
    @SerialName("session_count")
    val sessionCount: Int? = null,
    @SerialName("admin_password")
    val adminPassword: String? = null
)

fun ConfigEntity.toDomain(): Config {
    return Config(
        generation = generation!!,
        sessionCount = sessionCount!!,
        adminPassword = adminPassword!!
    )
}