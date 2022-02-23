package com.yapp.common.yds

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun YDSDropDownButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .wrapContentWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xffF7F7F9)
        ),
        elevation = null
    ) {
        Row {
            Text(
                text = text,
                color = Color(0xff6D747E),
                modifier = Modifier.align(alignment = Alignment.CenterVertically)
            )
            Icon(
                Icons.Filled.ArrowDropDown,
                contentDescription = "drop down",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
