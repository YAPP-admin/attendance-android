package com.yapp.data.model

import com.google.firebase.firestore.PropertyName
import com.yapp.domain.model.Version

data class VersionEntity(
    @PropertyName("min_version_code")
    val minVersionCode: Int? = null,
    @PropertyName("max_version_code")
    val maxVersionCode: Int? = null,
    @PropertyName("current_version")
    val currentVersion: String? = null,
)

fun VersionEntity.toDomain(): Version {
    return Version(
        minVersionCode = minVersionCode!!,
        maxVersionCode = maxVersionCode!!,
        currentVersion = currentVersion!!
    )
}
