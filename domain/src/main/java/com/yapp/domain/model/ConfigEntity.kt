package com.yapp.domain.model


data class ConfigEntity(
    val generation: Int,
    val sessionCount: Int,
    val adminPassword: String
)