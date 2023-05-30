package com.yapp.presentation.ui.admin.management.components.tablayout

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography


@Composable
internal fun YDSTabItemLayout(
    modifier: Modifier,
    isSelected: Boolean,
    label: String,
) {
    val textColor by animateColorAsState(
        targetValue = if (isSelected) {
            AttendanceTheme.colors.grayScale.Gray1200
        } else {
            AttendanceTheme.colors.grayScale.Gray400
        }
    )

    Column(modifier = modifier) {
        Text(
            text = label,
            style = AttendanceTypography.h3,
            color = textColor
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}