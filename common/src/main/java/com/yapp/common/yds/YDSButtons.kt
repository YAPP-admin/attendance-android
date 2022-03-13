package com.yapp.common.yds

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yapp.common.theme.Gray_200
import com.yapp.common.theme.Gray_400
import com.yapp.common.theme.Gray_800
import com.yapp.common.theme.Yapp_Orange
import com.yapp.common.theme.*
import java.lang.IllegalStateException

@Composable
fun YDSButtonLarge(
    text: String,
    modifier: Modifier = Modifier,
    state: YdsButtonState,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(10.dp),
        colors = when (state) {
            YdsButtonState.DISABLED -> {
                ButtonDefaults.buttonColors(
                    backgroundColor = Gray_400
                )
            }
            YdsButtonState.ENABLED,
            YdsButtonState.PRESSED
            -> {
                ButtonDefaults.buttonColors(
                    backgroundColor = Yapp_Orange
                )
            }
        },
        elevation = null
    ) {
        Text(
            text = text,
            color = Color.White
        )
    }
}

@Composable
fun YDSButtonMedium(
    text: String,
    modifier: Modifier = Modifier,
    state: YdsButtonState,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(10.dp),
        colors = when (state) {
            YdsButtonState.ENABLED -> {
                ButtonDefaults.buttonColors(
                    backgroundColor = Gray_1200
                )
            }
            YdsButtonState.PRESSED -> {
                ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xff42454A)
                )
            }
            YdsButtonState.DISABLED -> {
                throw IllegalStateException()
            }
        },
        elevation = null
    ) {
        Text(
            text = text,
            color = Color.White,
            style = AttendanceTypography.body1
        )
    }
}

@Composable
fun YDSButtonRegular(
    text: String,
    state: YdsButtonState,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .wrapContentWidth()
            .height(48.dp),
        shape = RoundedCornerShape(10.dp),
        colors = when (state) {
            YdsButtonState.ENABLED -> {
                ButtonDefaults.buttonColors(
                    backgroundColor = Yapp_Orange
                )
            }
            else -> {
                ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xffFA6027)
                )
            }
        },
        elevation = null
    ) {
        Text(
            text = text,
            color = Color.White
        )
    }
}

@Composable
fun YDSButtonSmall(
    text: String,
    state: YdsButtonState,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .wrapContentWidth()
            .height(32.dp),
        shape = RoundedCornerShape(10.dp),
        colors = when (state) {
            YdsButtonState.ENABLED -> {
                ButtonDefaults.buttonColors(
                    backgroundColor = Yapp_Orange
                )
            }
            else -> {
                ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xffFA6027)
                )
            }
        },
        elevation = null
    ) {
        Text(
            text = text,
            color = Color.White,
            style = AttendanceTypography.body2
        )
    }
}

@Composable
fun YDSChoiceButton(
    text: String,
    modifier: Modifier = Modifier,
    state: YdsButtonState,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .wrapContentWidth(),
        shape = RoundedCornerShape(60.dp),
        colors = when (state) {
            YdsButtonState.DISABLED -> {
                ButtonDefaults.buttonColors(
                    backgroundColor = Gray_200
                )
            }
            YdsButtonState.ENABLED,
            YdsButtonState.PRESSED
            -> {
                ButtonDefaults.buttonColors(
                    backgroundColor = Yapp_Orange
                )
            }
        },
        elevation = null
    ) {
        Text(
            text = text,
            style = AttendanceTypography.body1,
            color = when (state) {
                YdsButtonState.DISABLED -> {
                    Gray_800
                }
                YdsButtonState.ENABLED,
                YdsButtonState.PRESSED
                -> {
                    Color.White
                }
            }
        )
    }
}

enum class YdsButtonState {
    ENABLED, DISABLED, PRESSED
}