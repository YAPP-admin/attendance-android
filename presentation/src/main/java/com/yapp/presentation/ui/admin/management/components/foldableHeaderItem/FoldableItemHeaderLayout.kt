package com.yapp.presentation.ui.admin.management.components.foldableHeaderItem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.presentation.R
import com.yapp.presentation.ui.admin.management.components.AnimatedCounterText


@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
private fun FoldableItemHeaderLayoutPreview() {
    var state by remember {
        mutableStateOf(
            FoldableItemHeaderLayoutState(
                label = "Android 1",
                attendMemberCount = 4,
                allTeamMemberCount = 6
            )
        )
    }

    AttendanceTheme {
        Box(modifier = Modifier) {
            FoldableItemHeaderLayout(state = state)
        }
    }
}

@Composable
internal fun FoldableItemHeaderLayout(
    modifier: Modifier = Modifier,
    state: FoldableItemHeaderLayoutState,
    expanded: Boolean = false,
    onExpandClicked: (Boolean) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AttendanceTheme.colors.backgroundColors.background)
            .clickable { onExpandClicked(!expanded) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .width(0.dp)
                .weight(0.9F)
                .padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                text = state.label,
                textAlign = TextAlign.Start,
                style = AttendanceTypography.h3,
                color = AttendanceTheme.colors.grayScale.Gray1200
            )

            Spacer(modifier = Modifier.width(6.dp))

            AnimatedCounterText(
                count = state.attendMemberCount,
                style = AttendanceTypography.subtitle2.copy(color = AttendanceTheme.colors.mainColors.YappOrange)
            )

            Text(
                text = "/",
                textAlign = TextAlign.Start,
                style = AttendanceTypography.subtitle2,
                color = AttendanceTheme.colors.mainColors.YappOrange
            )

            AnimatedCounterText(
                count = state.allTeamMemberCount,
                style = AttendanceTypography.subtitle2.copy(color = AttendanceTheme.colors.mainColors.YappOrange)
            )
        }


        IconButton(
            modifier = Modifier
                .width(0.dp)
                .height(IntrinsicSize.Min)
                .weight(0.1F),
            onClick = { onExpandClicked(!expanded) }
        ) {
            Icon(
                painter = if (expanded) painterResource(id = R.drawable.icon_chevron_up) else painterResource(
                    id = R.drawable.icon_chevron_down
                ),
                tint = AttendanceTheme.colors.grayScale.Gray600,
                contentDescription = null
            )
        }
    }
}