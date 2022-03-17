package com.yapp.presentation.model.type


enum class PlatformType(val type: String) {
    ANDROID("Android"), IOS("iOS"), WEB("Web"), ALL_ROUNDER("All-Rounder");

    companion object {
        fun of(value: String): PlatformType {
            return when(value) {
                "Android" -> ANDROID
                "iOS" -> IOS
                "Web" -> WEB
                "All-Rounder" -> ALL_ROUNDER
                else -> error("잘못된 PlatformType 입니다.")
            }
        }
    }
}