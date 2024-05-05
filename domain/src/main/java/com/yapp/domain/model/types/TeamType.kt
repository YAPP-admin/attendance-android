package com.yapp.domain.model.types


enum class TeamType(val value: String) {
    ANDROID("Android"),
    IOS("iOS"),
    APP("APP"),
    WEB("Web"),
    FLUTTER("Flutter"),
    BASECAMP("BASECAMP"),
    NONE("None");

    companion object {
        fun from(rawValue: String): TeamType {
            return when(rawValue) {
                "NONE" -> NONE
                "ANDROID" -> ANDROID
                "IOS" -> IOS
                "APP" -> APP
                "WEB" -> WEB
                "FLUTTER" -> FLUTTER
                "BASECAMP" -> BASECAMP
                else -> error("$rawValue 에 해당하는 TeamType이 없습니다.")
            }
        }
    }

}
