package com.yapp.domain.model.types


enum class TeamType(val value: String) {
    ANDROID("Android"),
    IOS("iOS"),
    WEB("Web"),
    BASECAMP("BASECAMP"),
    NONE("None");

    companion object {
        fun from(rawValue: String): TeamType {
            return when(rawValue) {
                "ANDROID" -> ANDROID
                "IOS" -> IOS
                "WEB" -> WEB
                "BASECAMP" -> BASECAMP
                "NONE" -> NONE
                else -> error("$rawValue 에 해당하는 TeamType이 없습니다.")
            }
        }
    }

}
