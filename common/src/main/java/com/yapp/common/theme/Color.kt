package com.yapp.common.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


// Light Colors
val Light_Yapp_Orange = Color(0XFFFA6027)
val Light_Yapp_OrangeAlpha = Color(0X1AFA6027)
val Light_Yapp_OrangePressed = Color(0XFFFF7744)
val Light_Yapp_Yellow = Color(0XFFFFD74B)

val Light_Etc_Green = Color(0XFF27AE60)
val Light_Etc_Yellow = Color(0XFFFFCE1F)
val Light_Etc_Yellow_Font = Color(0XFFFFBB0D)
val Light_Etc_Red = Color(0XFFEE5120)

val Light_Gray_1200 = Color(0XFF313439)
val Light_Gray_1000 = Color(0XFF42454A)
val Light_Gray_800 = Color(0XFF69707B)
val Light_Gray_600 = Color(0XFF9BA3AE)
val Light_Gray_400 = Color(0XFFCCD2DA)
val Light_Gray_300 = Color(0XFFE8EAED)
val Light_Gray_200 = Color(0XFFF3F5F8)

val Light_Background = Color(0XFFFFFFFF)
val Light_Background_Base = Color(0XFFF3F5F8)
val Light_Background_Elevated = Color(0XFFFFFFFF)

val Light_Pressed_Light = Color(0XFFFFFFFF)
val Light_Pressed_Dark = Color(0XFF98A3AE)

// Dark Colors
val Dark_Yapp_Orange = Color(0XFFFC7948)
val Dark_Yapp_OrangeAlpha = Color(0X1AFC7948)
val Dark_Yapp_OrangePressed = Color(0XFFEF8D64)
val Dark_Yapp_Yellow = Color(0XFFFFD74B)

val Dark_Etc_Green = Color(0XFF3DBB72)
val Dark_Etc_Yellow = Color(0XFFFFCE1F)
val Dark_Etc_Yellow_Font = Color(0XFFFFCE1F)
val Dark_Etc_Red = Color(0XFFEB6D45)

val Dark_Gray_1200 = Color(0XFFF7F9FB)
val Dark_Gray_1000 = Color(0XFFE5ECF3)
val Dark_Gray_800 = Color(0XFFCBD1DC)
val Dark_Gray_600 = Color(0XFF636877)
val Dark_Gray_400 = Color(0XFF383944)
val Dark_Gray_300 = Color(0XFF282A31)
val Dark_Gray_200 = Color(0XFF202127)

val Dark_Background = Color(0XFF16171D)
val Dark_Background_Base = Color(0XFF09090B)
val Dark_Background_Elevated = Color(0XFF282A31)

val Dark_Pressed_Light = Color(0XFF141414)
val Dark_Pressed_Dark = Color(0XFF141414)

// Brush (Gradient)
val Yapp_Gradient = Brush.linearGradient(
    colors = listOf(Light_Yapp_Orange, Light_Yapp_Yellow),
    start = Offset(150F, 40F),
    end = Offset(450F, 40F)
)

@Immutable
data class SemanticBackgroundColors(
    val backgroundBase: Color,
    val background: Color,
    val backgroundElevated: Color
)

@Immutable
data class SemanticPressedColors(
    val pressLight: Color,
    val pressDark: Color
)

@Immutable
data class YDSColors(
    val backgroundColors: SemanticBackgroundColors,
    val pressedColors: SemanticPressedColors,
    val grayScale: GrayScale,
    val mainColors: MainColors,
    val etcColors: EtcColors
)

@Immutable
data class GrayScale(
    val Gray200: Color,
    val Gray300: Color,
    val Gray400: Color,
    val Gray600: Color,
    val Gray800: Color,
    val Gray1000: Color,
    val Gray1200: Color,
)

@Immutable
data class MainColors(
    val YappOrange: Color,
    val YappOrangeAlpha: Color,
    val YappOrangePressed: Color,
    val YappYellow: Color,
    val YappGradient: Brush
)

@Immutable
data class EtcColors(
    val EtcGreen: Color,
    val EtcYellow: Color,
    val EtcYellowFont: Color,
    val EtcRed: Color,
)

val LocalYDSColors = staticCompositionLocalOf {
    YDSColors(
        backgroundColors = SemanticBackgroundColors(
            background = Color.Unspecified,
            backgroundBase = Color.Unspecified,
            backgroundElevated = Color.Unspecified,
        ),
        pressedColors = SemanticPressedColors(
            pressLight = Color.Unspecified,
            pressDark = Color.Unspecified
        ),
        grayScale = GrayScale(
            Gray200 = Color.Unspecified,
            Gray300 = Color.Unspecified,
            Gray400 = Color.Unspecified,
            Gray600 = Color.Unspecified,
            Gray800 = Color.Unspecified,
            Gray1000 = Color.Unspecified,
            Gray1200 = Color.Unspecified
        ),
        mainColors = MainColors(
            YappOrange = Color.Unspecified,
            YappOrangeAlpha = Color.Unspecified,
            YappOrangePressed = Color.Unspecified,
            YappYellow = Color.Unspecified,
            YappGradient = Brush.linearGradient(colors = emptyList())
        ),
        etcColors = EtcColors(
            EtcGreen = Color.Unspecified,
            EtcYellow = Color.Unspecified,
            EtcYellowFont = Color.Unspecified,
            EtcRed = Color.Unspecified
        )
    )
}