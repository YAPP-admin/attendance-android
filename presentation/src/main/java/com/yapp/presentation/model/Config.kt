package com.yapp.presentation.model

import com.yapp.domain.model.ConfigEntity


data class Config(
    val generation: Int,
    val sessionCount: Int,
    val adminPassword: String
) {
    companion object {
        fun ConfigEntity.mapTo(): Config {
            return Config(
                generation = generation,
                sessionCount = sessionCount,
                adminPassword = adminPassword
            )
        }
    }

}