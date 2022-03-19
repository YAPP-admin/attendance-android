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
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.theme.Yapp_Orange
import com.yapp.common.theme.Yapp_OrangeAlpha

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
            .background(color = Yapp_OrangeAlpha)
    ) {
        Text(
            text = text,
            color = Yapp_Orange,
            style = AttendanceTypography.body1,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}