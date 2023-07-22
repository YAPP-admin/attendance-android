package com.yapp.data.model

import com.yapp.domain.model.Version
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VersionEntity(
    @SerialName("min_version_code")
    val minVersionCode: Int? = null,
    @SerialName("max_version_code")
    val maxVersionCode: Int? = null,
    @SerialName("current_version")
    val currentVersion: String? = null,
)

fun VersionEntity.toDomain(): Version {
    return Version(
        minVersionCode = minVersionCode!!,
        maxVersionCode = maxVersionCode!!,
        currentVersion = currentVersion!!
    )
}
