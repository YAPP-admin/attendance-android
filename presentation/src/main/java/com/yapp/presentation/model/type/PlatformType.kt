package com.yapp.presentation.model.type


enum class PlatformType {
    ANDROID, IOS, WEB;

    companion object {
        fun of(value: String): PlatformType {
            return when(value) {
                "Android" -> ANDROID
                "Ios" -> IOS
                "Web" -> WEB
                else -> error("잘못된 PlatformType 입니다.")
            }
        }
    }
}