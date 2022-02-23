package com.yapp.common.yds

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.Gray_1200
import com.yapp.common.theme.Pretendard

@Composable
fun YDSAppBar(
    title: String = "",
    backgroundColor: Color = Color.White,
    onClickBackButton: () -> Unit,
    onClickSettings: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(
            text = title,
            color = Gray_1200,
            fontFamily = Pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        ) },
        backgroundColor = backgroundColor,
        elevation = 0.dp,
        navigationIcon = {
            IconButton(onClick = { onClickBackButton() }) {
                //화살표로 변경이 필요함.
                Icon(Icons.Filled.Menu, contentDescription = null)
            }
        },
        actions = {
            onClickSettings?.let {
                IconButton(onClick = { it() }) {
                    Icon(Icons.Filled.Settings, contentDescription = "Setting")
                }
            }
        }
    )
}

@Composable
@Preview(name = "AppBar")
private fun YDSAppBarPreview() {
    AttendanceTheme {
        YDSAppBar(
            title = "페이지명",
            onClickBackButton = {},
            onClickSettings = {}
        )
    }
}