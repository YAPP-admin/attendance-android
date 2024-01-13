package com.yapp.common.yds

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography

@ExperimentalComposeUiApi
@Composable
fun YDSPopupDialog(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    negativeButtonText: String,
    positiveButtonText: String,
    onClickNegativeButton: () -> Unit,
    onClickPositiveButton: () -> Unit,
    onDismiss: () -> Unit,
) {
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
                Row(
                    modifier = Modifier.padding(top = 18.dp)
                ) {
                    Button(
                        onClick = onClickNegativeButton,
                        modifier = Modifier
                            .width(0.dp)
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
                            color = AttendanceTheme.colors.grayScale.Gray800,
                            overflow = TextOverflow.Ellipsis
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

@Composable
fun YDSTextFieldPopupDialog(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    negativeButtonText: String,
    positiveButtonText: String,
    onClickNegativeButton: () -> Unit,
    onClickPositiveButton: () -> Unit,
    textFieldInitValue: String,
    textFieldHint: String,
    textFieldChangedListener: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var isTextFieldFocused by remember { mutableStateOf(false) }

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
                TextField(
                    value = textFieldInitValue,
                    onValueChange = { textFieldChangedListener(it) },
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
                                text = textFieldHint,
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
                Row(
                    modifier = Modifier.padding(top = 18.dp)
                ) {
                    Button(
                        onClick = onClickNegativeButton,
                        modifier = Modifier
                            .width(0.dp)
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
                            color = AttendanceTheme.colors.grayScale.Gray800,
                            overflow = TextOverflow.Ellipsis
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

@Composable
fun YDSSingleButtonPopupDialog(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    buttonText: String,
    onClickButton: () -> Unit,
    onDismiss: () -> Unit,
) {
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
                Button(
                    onClick = onClickButton,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp)
                        .height(46.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AttendanceTheme.colors.etcColors.EtcRed
                    ),
                    shape = RoundedCornerShape(10.dp),
                    elevation = null
                ) {
                    Text(
                        text = buttonText,
                        style = AttendanceTypography.body1,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(device = "spec:width=480px,height=800px,dpi=240")
@Composable
private fun YDSPopupDialogPreview() {
    AttendanceTheme {
        YDSPopupDialog(
            title = "팀을 선택해 주세요",
            content = "원활한 출석체크를 위해\\n팀과 직무 선택이 꼭 필요해요!",
            negativeButtonText = "취소",
            positiveButtonText = "팀 선택하기",
            onClickNegativeButton = { },
            onClickPositiveButton = { },
            onDismiss = { }
        )
    }
}

@Preview(device = "spec:width=480px,height=800px,dpi=240")
@Composable
private fun YDSTextFieldPopupDialogPreview() {
    AttendanceTheme {
        YDSTextFieldPopupDialog(
            title = "암호를 대라!",
            content = "코드 넘버를 입력해주세요",
            negativeButtonText = "취소",
            positiveButtonText = "확인",
            onClickNegativeButton = { },
            onClickPositiveButton = { },
            textFieldInitValue = "",
            textFieldHint = "****",
            textFieldChangedListener = { },
            onDismiss = { },
        )
    }
}


@Preview(device = "spec:width=480px,height=800px,dpi=240")
@Composable
private fun YDSSingleButtonPopupDialogPreview() {
    AttendanceTheme {
        YDSSingleButtonPopupDialog(
            title = "업데이트가 필요해요!",
            content = "더 멋진 기능을 사용하기 위해\n지금 바로 업데이트를 진행해주세요.",
            buttonText = "업데이트",
            onClickButton = { },
            onDismiss = { }
        )
    }
}
