package com.yapp.common.yds

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography

@Composable
fun YDSBox(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 24.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = AttendanceTheme.colors.mainColors.YappOrangeAlpha)
    ) {
        Text(
            text = text,
            color = AttendanceTheme.colors.mainColors.YappOrange,
            style = AttendanceTypography.body1,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}