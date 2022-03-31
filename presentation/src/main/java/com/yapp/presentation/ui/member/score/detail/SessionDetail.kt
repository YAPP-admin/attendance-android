package com.yapp.presentation.ui.member.score.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yapp.common.theme.*
import com.yapp.common.yds.AttendanceType
import com.yapp.common.yds.YDSAppBar
import com.yapp.presentation.model.Session

@Composable
fun SessionDetail(
    navParam: SessionDetailNavParam,
    onClickBackButton: () -> Unit,
) {
    Scaffold(
        topBar = {
            YDSAppBar(
                title = navParam.title,
                onClickBackButton = onClickBackButton
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 데이터 넣어야 함
                Icon(
                    painterResource(id = AttendanceType.ATTEND.icon),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
                Text(
                    text = "출석",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    color = Etc_Green,
                )

                Text(
                    text = navParam.date,
                    style = AttendanceTypography.body1,
                    color = Gray_600
                )
            }

            Text(
                text = navParam.title,
                modifier = Modifier.padding(top = 28.dp),
                style = AttendanceTypography.h1,
                color = Gray_1000
            )

            Text(
                text = navParam.description,
                modifier = Modifier.padding(top = 12.dp),
                style = AttendanceTypography.body1,
                color = Gray_800
            )
        }
    }
}