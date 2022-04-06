package com.yapp.presentation.model.type


enum class TeamType(val displayName: String) {
    ANDROID("Android"), IOS("iOS"), WEB("Web"), ALL_ROUNDER("All-Rounder");

    companion object {
        fun of(value: String): TeamType {
            return when(value) {
                "Android" -> ANDROID
                "iOS" -> IOS
                "Web" -> WEB
                "All-Rounder" -> ALL_ROUNDER
                else -> error("잘못된 TeamType 입니다.")
            }
        }
    }
}