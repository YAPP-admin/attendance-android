package com.yapp.domain.model

data class Version(
    val minVersionCode: Long,
    val maxVersionCode: Long,
    val currentVersion: String,
)
