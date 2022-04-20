package com.yapp.common.yds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.common.R
import com.yapp.common.theme.*

@Composable
fun YDSAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    onClickBackButton: (() -> Unit)? = null,
    onClickSettings: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp) // todo sehee 이거 하드코딩 어케 못하나...
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.icon_back),
            tint = Color.Unspecified,
            contentDescription = null,
            modifier = Modifier
                .alpha(if (onClickBackButton == null) 0f else 1f)
                .clip(RoundedCornerShape(30.dp))
                .clickable {
                    onClickBackButton?.invoke()
                }
        )

        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(start = 20.dp, end = 20.dp),
            textAlign = TextAlign.Center,
            style = AttendanceTypography.h3,
            color = Gray_1200,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Icon(
            imageVector = Icons.Filled.Settings,
            tint = Gray_600,
            contentDescription = null,
            modifier = Modifier
                .alpha(if (onClickSettings == null) 0f else 1f)
                .clip(RoundedCornerShape(30.dp))
                .clickable {
                    onClickSettings?.invoke()
                }
        )
    }
}

@Preview("appBarPreview")
@Composable
fun AppBarPreview() {
    YDSAppBar(
        onClickBackButton = null,
        title = "test",
        onClickSettings = { }
    )
}