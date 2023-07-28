package com.yapp.common.yds

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography

@ExperimentalComposeUiApi
@Composable
fun YDSPopupDialog(
    title: String,
    content: String,
    modifier: Modifier = Modifier,
    negativeButtonText: String? = null,
    positiveButtonText: String,
    onClickNegativeButton: (() -> Unit)? = null,
    onClickPositiveButton: () -> Unit,
    editTextInitValue: String = "",
    editTextHint: String? = null,
    editTextChangedListener: (String) -> Unit = { },
    onDismiss: () -> Unit
) {
    var isTextFieldFocused by remember { mutableStateOf<Boolean>(false) }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(10.dp),
            color = AttendanceTheme.colors.backgroundColors.backgroundElevated
        ) {
            Column(
                modifier = Modifier
                    .background(AttendanceTheme.colors.backgroundColors.backgroundElevated)
                    .padding(24.dp)
            ) {
                Text(
                    text = title,
                    style = AttendanceTypography.h3,
                    color = AttendanceTheme.colors.grayScale.Gray1200
                )
                Text(
                    text = content,
                    modifier = Modifier.padding(top = 8.dp),
                    style = AttendanceTypography.body1,
                    color = AttendanceTheme.colors.grayScale.Gray800
                )

                if (editTextHint != null) {
                    TextField(
                        value = editTextInitValue,
                        onValueChange = { editTextChangedListener(it) },
                        singleLine = true,
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .onFocusEvent { state ->
                                isTextFieldFocused = state.isFocused
                            }
                            .background(
                                color = AttendanceTheme.colors.grayScale.Gray200,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        placeholder = {
                            if (!isTextFieldFocused) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = editTextHint,
                                    textAlign = TextAlign.Center,
                                    color = AttendanceTheme.colors.grayScale.Gray400,
                                )
                            }
                        },
                        textStyle = AttendanceTypography.body2.copy(
                            color = AttendanceTheme.colors.grayScale.Gray800,
                            textAlign = TextAlign.Center
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = AttendanceTheme.colors.grayScale.Gray800,
                            disabledTextColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation()
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 18.dp)
                ) {
                    if (onClickNegativeButton != null && negativeButtonText != null) {
                        Button(
                            onClick = onClickNegativeButton,
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .padding(end = 6.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = AttendanceTheme.colors.grayScale.Gray200
                            ),
                            shape = RoundedCornerShape(10.dp),
                            elevation = null
                        ) {
                            Text(
                                text = negativeButtonText,
                                style = AttendanceTypography.body1,
                                color = AttendanceTheme.colors.grayScale.Gray800
                            )
                        }
                    }

                    val positiveButtonPadding =
                        if (onClickNegativeButton != null && negativeButtonText != null)
                            Modifier.padding(horizontal = 12.dp)
                        else Modifier.padding(start = 6.dp)

                    Button(
                        onClick = onClickPositiveButton,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .then(positiveButtonPadding),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = AttendanceTheme.colors.etcColors.EtcRed // EtcRed or YappOrange
                        ),
                        shape = RoundedCornerShape(10.dp),
                        elevation = null
                    ) {
                        Text(
                            text = positiveButtonText,
                            style = AttendanceTypography.body1,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
