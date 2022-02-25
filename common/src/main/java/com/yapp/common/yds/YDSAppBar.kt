package com.yapp.common.yds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.common.R
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.Gray_1000
import com.yapp.common.theme.Gray_1200

@Composable
fun YDSAppBar(
    title: String = "",
    backgroundColor: Color = Color.White,
    onClickBackButton: (() -> Unit)? = null,
    onClickSettings: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Gray_1200
            )
        },
        backgroundColor = backgroundColor,
        elevation = 0.dp,
        navigationIcon = {
            IconButton(onClick = { onClickBackButton?.invoke() }) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = null,
                    Modifier
                        .alpha(if (onClickBackButton == null) 0f else 1f)
                        .clickable(enabled = onClickBackButton != null) {}
                )
            }
        },
        actions = {
            IconButton(onClick = { onClickSettings?.invoke() }) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Setting",
                    Modifier
                        .alpha(if (onClickSettings == null) 0f else 1f)
                        .clickable(enabled = onClickBackButton != null) {}
                )
            }
        }
    )
}

@Composable
@Preview(name = "AppBar")
private fun YDSAppBarPreview() {
    AttendanceTheme {
        YDSAppBar(
            title = "페이지명"
        )
    }
}