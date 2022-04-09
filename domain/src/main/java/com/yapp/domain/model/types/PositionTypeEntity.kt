package com.yapp.domain.model.types


enum class PositionTypeEntity {
    DEV_ANDROID, DEV_WEB, DEV_IOS, DEV_SERVER, DESIGNER, PROJECT_MANAGER;

    companion object {
        fun of(value: String): PositionTypeEntity {
            return when(value) {
                "DEV_ANDROID" -> DEV_ANDROID
                "DEV_WEB" -> DEV_WEB
                "DEV_IOS" -> DEV_IOS
                "DEV_SERVER" -> DEV_SERVER
                "DESIGNER" -> DESIGNER
                "PROJECT_MANAGER" -> PROJECT_MANAGER
                else -> error("잘못된 PositionType 입니다.")
            }
        }
    }
}