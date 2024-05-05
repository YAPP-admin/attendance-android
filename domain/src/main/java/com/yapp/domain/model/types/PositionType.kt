package com.yapp.domain.model.types


enum class PositionType(val value: String) {
    PROJECT_MANAGER("PM"),
    DESIGNER("UI/UX Design"),
    DEV_ANDROID("Android"),
    DEV_IOS("iOS"),
    DEV_WEB("Web"),
    DEV_SERVER("Server"),
    DEV_FLUTTER("Flutter"),;

    companion object {
        fun of(value: String): PositionType {
            return when (value) {
                "PROJECT_MANAGER" -> PROJECT_MANAGER
                "DESIGNER" -> DESIGNER
                "DEV_ANDROID" -> DEV_ANDROID
                "DEV_IOS" -> DEV_IOS
                "DEV_WEB" -> DEV_WEB
                "DEV_SERVER" -> DEV_SERVER
                "DEV_FLUTTER" -> DEV_FLUTTER
                else -> error("잘못된 PositionType 입니다.")
            }
        }
    }
}
