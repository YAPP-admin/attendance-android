package com.yapp.domain.model.types


enum class TeamType(val value: String) {
    ANDROID("Android"),
    IOS("iOS"),
    WEB("Web"),
    ALL_ROUNDER("All-Rounder");

    companion object {
        fun from(rawValue: String): TeamType {
            return when(rawValue) {
                "Android" -> ANDROID
                "iOS" -> IOS
                "Web" -> WEB
                "All-Rounder" -> ALL_ROUNDER
                else -> error("$rawValue 에 해당하는 TeamType이 없습니다.")
            }
        }
    }

}