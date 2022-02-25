package com.yapp.presentation.ui.member.todaysession

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yapp.common.yds.YDSAppBar

@Composable
fun TodaySession(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            YDSAppBar(
                onClickSettings = {}
            )
        },
        modifier = modifier
            .fillMaxSize()
    ) {
        Text("hhello!!!")
    }
}