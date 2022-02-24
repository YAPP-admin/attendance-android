package com.yapp.common.yds

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun YDSFullButtonContainer(
    text: String,
    modifier: Modifier = Modifier,
    state: YdsButtonState,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = when (state) {
            YdsButtonState.DISABLED -> {
                ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xffF7F7F9)
                )
            }
            YdsButtonState.ENABLED -> {
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
fun YDSChoiceButtonContainer(
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
                    backgroundColor = Color(0xffF7F7F9)
                )
            }
            YdsButtonState.ENABLED -> {
                ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xffFA6027)
                )
            }
        },
        elevation = null
    ) {
        Text(
            text = text,
            color = when (state) {
                YdsButtonState.DISABLED -> {
                    Color(0xff69707B)
                }
                YdsButtonState.ENABLED -> {
                    Color.White
                }
            }
        )
    }
}

enum class YdsButtonState {
    ENABLED, DISABLED
}