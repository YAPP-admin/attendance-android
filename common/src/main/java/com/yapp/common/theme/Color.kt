package com.yapp.common.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


val Yapp_Orange         = Color(0XFFFA6027)
val Yapp_OrangePressed  = Color(0XFFFF7744)
val Yapp_Yellow         = Color(0XFFFFD74B)
val Yapp_Gradient       = Brush.linearGradient(
                                colors  = listOf(Yapp_Orange, Yapp_Yellow),
                                start   = Offset(150F, 40F),
                                end     = Offset(450F, 40F))

val Etc_Green       = Color(0XFF27AE60)
val Etc_Yellow      = Color(0XFFFFCE1F)
val Etc_Yellow_Font = Color(0XFFFFBB0D)
val Etc_Red         = Color(0XFFEE5120)

val Gray_1200   = Color(0XFF313439)
val Gray_1000   = Color(0XFF42454A)
val Gray_800    = Color(0XFF69707B)
val Gray_600    = Color(0XFF9BA3AE)
val Gray_400    = Color(0XFFCCD2DA)
val Gray_300    = Color(0XFFE8EAED)
val Gray_200    = Color(0XFFF7F7F9)
