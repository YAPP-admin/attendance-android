package com.yapp.common.yds

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yapp.common.R.drawable
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography

@Composable
fun YDSDropDownButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .wrapContentWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = AttendanceTheme.colors.grayScale.Gray200),
        elevation = null
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Crossfade(targetState = text) {
                Text(
                    text = it,
                    modifier = Modifier
                        .height(20.dp)
                        .align(alignment = Alignment.CenterVertically),
                    style = AttendanceTypography.subtitle2,
                    color = AttendanceTheme.colors.grayScale.Gray800
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = drawable.icon_rounded_drop_down),
                contentDescription = "drop down",
                tint = AttendanceTheme.colors.grayScale.Gray800
            )
        }
    }
}
