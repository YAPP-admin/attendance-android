package com.yapp.presentation.ui.member.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.theme.Gray_200
import com.yapp.common.theme.Gray_800
import com.yapp.common.theme.Yapp_Orange
import com.yapp.common.yds.YDSAppBar
import com.yapp.presentation.R

@Composable
fun Name(
    viewModel: NameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { YDSAppBar(onClickBackButton = {}) },
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Column() {
                Title()
                InputName()
            }
        }
    }
}

@Composable
fun Title() {
    Column() {

        var text by remember { mutableStateOf("") }

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(id = R.string.name_title),
            color = Color.Black,
            style = AttendanceTypography.h1
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.name_subtitle),
            color = Gray_800,
            style = AttendanceTypography.body1
        )
        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
fun InputName() {

    var text by remember { mutableStateOf("") }

    BasicTextField(
        value = text,
        onValueChange = { text = it },
        singleLine = true,
        textStyle = AttendanceTypography.body1,
        cursorBrush = SolidColor(Yapp_Orange),
        decorationBox = { innerTextField ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Gray_200, shape = RoundedCornerShape(50.dp))
            ) {

                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(20.dp))
                    if (text.isBlank()) {
                        Text(
                            text = stringResource(id = R.string.name_example_hint),
                            color = Gray_200,
                            style = AttendanceTypography.body1
                        )
                    }
                    else {
                        innerTextField()
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                }
                Spacer(modifier = Modifier.height(20.dp))

            }
        }
    )
}

@Preview(
    widthDp = 320,
)
@Composable
fun P() {


}