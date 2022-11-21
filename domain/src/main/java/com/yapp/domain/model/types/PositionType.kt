package com.yapp.domain.model.types


enum class PositionType(val value: String) {
    PROJECT_MANAGER("PM"),
    DESIGNER("UI/UX Design"),
    DEV_ANDROID("Android"),
    DEV_IOS("iOS"),
    DEV_WEB("Web"),
    DEV_SERVER("Server");

    companion object {
        fun of(value: String): PositionType {
            return when (value) {
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