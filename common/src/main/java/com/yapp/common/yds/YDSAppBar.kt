package com.yapp.common.yds

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.yapp.common.theme.AttendanceTheme

@Composable
fun YDSAppBar(
    title: String = "",
    onClickBackButton: () -> Unit,
    onClickSettings: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(text = title) },
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