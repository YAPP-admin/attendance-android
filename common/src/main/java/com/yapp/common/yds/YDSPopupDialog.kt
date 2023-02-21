package com.yapp.common.yds

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yapp.common.theme.*

@ExperimentalComposeUiApi
@Composable
fun YDSPopupDialog(
    title: String,
    content: String,
    modifier: Modifier = Modifier,
    negativeButtonText: String,
    positiveButtonText: String,
    onClickNegativeButton: () -> Unit,
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
                .fillMaxSize()
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

                    Button(
                        onClick = onClickPositiveButton,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .padding(start = 6.dp),
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
