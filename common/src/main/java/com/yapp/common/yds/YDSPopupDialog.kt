package com.yapp.common.yds

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.yapp.common.theme.*

@Composable
fun YDSPopupDialog(
    title: String,
    content: String,
    negativeButtonText: String,
    positiveButtonText: String,
    onClickNegativeButton: () -> Unit,
    onClickPositiveButton: () -> Unit,
    editTextInitValue: String = "",
    editTextHint: String? = null,
    editTextChangedListener: (String) -> Unit = { },
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .padding(8.dp),
            shape = RoundedCornerShape(10.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                Text(
                    text = title,
                    style = AttendanceTypography.h3,
                    color = Gray_1200
                )
                Text(
                    text = content,
                    modifier = Modifier.padding(top = 8.dp),
                    style = AttendanceTypography.body1,
                    color = Gray_800
                )

                if (editTextHint != null) {
                    TextField(
                        value = editTextInitValue,
                        onValueChange = { editTextChangedListener(it) },
                        singleLine = true,
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .background(color = Gray_200, shape = RoundedCornerShape(10.dp)),
                        placeholder = {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = editTextHint,
                                textAlign = TextAlign.Center,
                                color = Gray_400,
                            )
                        },
                        textStyle = AttendanceTypography.body2.copy(
                            color = Gray_800,
                            textAlign = TextAlign.Center
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Gray,
                            disabledTextColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
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
                            backgroundColor = Gray_200,
                        ),
                        shape = RoundedCornerShape(10.dp),
                        elevation = null
                    ) {
                        Text(
                            text = negativeButtonText,
                            style = AttendanceTypography.body1,
                            color = Gray_800
                        )
                    }

                    Button(
                        onClick = onClickPositiveButton,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .padding(start = 6.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Etc_Red,
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
