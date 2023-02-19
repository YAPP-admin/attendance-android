package com.yapp.common.yds

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yapp.common.R
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography

@Composable
fun YDSEmptyScreen() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(AttendanceTheme.colors.backgroundColors.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AttendanceTheme.colors.backgroundColors.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.illust_empty),
                contentDescription = null
            )
            Text(
                text = stringResource(id = R.string.empty_message),
                style = AttendanceTypography.h3,
                color = AttendanceTheme.colors.grayScale.Gray600,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}
