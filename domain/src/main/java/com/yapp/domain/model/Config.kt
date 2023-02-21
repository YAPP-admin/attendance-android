package com.yapp.domain.model


data class Config(
    val generation: Int,
    val sessionCount: Int,
    val adminPassword: String
)