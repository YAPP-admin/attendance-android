package com.yapp.presentation.model.type


enum class PlatformType {
    ANDROID, IOS, WEB, ALL_ROUNDER;

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