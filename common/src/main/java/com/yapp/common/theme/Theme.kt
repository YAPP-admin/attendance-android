package com.yapp.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


@Composable
fun AttendanceTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        provideDarkThemeColors()
    } else {
        provideLightThemeColors()
    }

    CompositionLocalProvider(
        LocalYDSColors provides colors
    ) {
        MaterialTheme(
            typography = AttendanceTypography,
            shapes = Shapes,
            content = content
        )
    }
}

object AttendanceTheme {
    val colors: YDSColors
        @Composable
        get() = LocalYDSColors.current
}

private fun provideLightThemeColors(): YDSColors {
    return YDSColors(
        backgroundColors = SemanticBackgroundColors(
            background = Light_Background,
            backgroundBase = Light_Background_Base,
            backgroundElevated = Light_Background_Elevated,
        ),
        pressedColors = SemanticPressedColors(
            pressLight = Light_Pressed_Light,
            pressDark = Light_Pressed_Dark
        ),
        grayScale = GrayScale(
            Gray200 = Light_Gray_200,
            Gray300 = Light_Gray_300,
            Gray400 = Light_Gray_400,
            Gray600 = Light_Gray_600,
            Gray800 = Light_Gray_800,
            Gray1000 = Light_Gray_1000,
            Gray1200 = Light_Gray_1200
        ),
        mainColors = MainColors(
            YappOrange = Light_Yapp_Orange,
            YappOrangeAlpha = Light_Yapp_OrangeAlpha,
            YappOrangePressed = Light_Yapp_OrangePressed,
            YappYellow = Light_Yapp_Yellow,
            YappGradient = Yapp_Gradient
        ),
        etcColors = EtcColors(
            EtcGreen = Light_Etc_Green,
            EtcYellow = Light_Etc_Yellow,
            EtcYellowFont = Light_Etc_Yellow_Font,
            EtcRed = Light_Etc_Red
        )
    )
}

private fun provideDarkThemeColors(): YDSColors {
    return YDSColors(
        backgroundColors = SemanticBackgroundColors(
            background = Dark_Background,
            backgroundBase = Dark_Background_Base,
            backgroundElevated = Dark_Background_Elevated,
        ),
        pressedColors = SemanticPressedColors(
            pressLight = Dark_Pressed_Light,
            pressDark = Dark_Pressed_Dark
        ),
        grayScale = GrayScale(
            Gray200 = Dark_Gray_200,
            Gray300 = Dark_Gray_300,
            Gray400 = Dark_Gray_400,
            Gray600 = Dark_Gray_600,
            Gray800 = Dark_Gray_800,
            Gray1000 = Dark_Gray_1000,
            Gray1200 = Dark_Gray_1200
        ),
        mainColors = MainColors(
            YappOrange = Dark_Yapp_Orange,
            YappOrangeAlpha = Dark_Yapp_OrangeAlpha,
            YappOrangePressed = Dark_Yapp_OrangePressed,
            YappYellow = Dark_Yapp_Yellow,
            YappGradient = Yapp_Gradient
        ),
        etcColors = EtcColors(
            EtcGreen = Dark_Etc_Green,
            EtcYellow = Dark_Etc_Yellow,
            EtcYellowFont = Dark_Etc_Yellow_Font,
            EtcRed = Dark_Etc_Red
        )
    )
}


