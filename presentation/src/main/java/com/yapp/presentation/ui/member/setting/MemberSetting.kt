package com.yapp.presentation.ui.member.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MemberSetting(viewModel: MemberSettingViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsState()
}