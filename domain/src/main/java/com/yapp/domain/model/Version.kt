package com.yapp.domain.model

data class Version(
    val minVersionCode: Int,
    val maxVersionCode: Int,
    val currentVersion: String,
)
