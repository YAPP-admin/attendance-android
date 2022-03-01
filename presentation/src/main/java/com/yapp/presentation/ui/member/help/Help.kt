package com.yapp.presentation.ui.member.help

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.yapp.common.yds.YDSAppBar
import com.yapp.presentation.R

@Composable
fun Help() {
    Scaffold(
        topBar = {
            YDSAppBar(
                title = stringResource(id = R.string.help_title),
                onClickBackButton = {}
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) {

    }
}
