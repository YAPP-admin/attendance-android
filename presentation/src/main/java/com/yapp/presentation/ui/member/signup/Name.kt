package com.yapp.presentation.ui.member.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.theme.Gray_200
import com.yapp.common.theme.Gray_800
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
            InputName()
        }
    }
}

@Composable
fun InputName() {
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
fun NameTextFieldBackground() {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(color = Gray_200, shape = RoundedCornerShape(50.dp))
    )
}

@Preview(
    showBackground = true,
    widthDp = 320,
)
@Composable
fun P() {

    var text by remember { mutableStateOf("") }

    BaseTextField()
//    TextField(
//        value = text,
//        onValueChange = { text = it },
//        textStyle = AttendanceTypography.body1,
//        placeholder = {
//            Text(
//                text = stringResource(id = R.string.name_example_hint),
//                style = AttendanceTypography.body1,
//                color = Gray_400
//            )
//        },
//    )
}