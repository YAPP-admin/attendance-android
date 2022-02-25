package com.yapp.common.yds

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.theme.Etc_Red
import com.yapp.common.theme.Gray_200
import com.yapp.common.theme.Gray_800

@Composable
fun YDSPopupDialog(
    title: String,
    content: String,
    negativeButtonText: String,
    positiveButtonText: String,
    onClickNegativeButton: () -> Unit,
    onClickPositiveButton: () -> Unit,
) {
    Dialog(onDismissRequest = { }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(10.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                Text(
                    text = title,
                    style = AttendanceTypography.h3
                )
                Text(
                    text = content,
                    modifier = Modifier.padding(top = 8.dp),
                    style = AttendanceTypography.body1
                )

                Row(
                    modifier = Modifier.padding(top = 18.dp)
                ) {
                    Button(
                        onClick = onClickNegativeButton,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 6.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Gray_200,
                        ),
                        shape = RoundedCornerShape(10.dp),
                        elevation = null
                    ) {
                        Text(
                            text = negativeButtonText,
                            color = Gray_800
                        )
                    }

                    Button(
                        onClick = onClickPositiveButton,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 6.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Etc_Red,
                        ),
                        shape = RoundedCornerShape(10.dp),
                        elevation = null
                    ) {
                        Text(
                            text = positiveButtonText,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
