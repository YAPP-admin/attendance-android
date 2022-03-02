package com.yapp.presentation.ui.member.main

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.theme.Gray_1000
import com.yapp.common.theme.Gray_600
import com.yapp.common.yds.*
import com.yapp.presentation.R
import com.yapp.presentation.ui.login.Login
import com.yapp.presentation.ui.member.detail.MemberScore
import com.yapp.presentation.ui.member.main.MemberMainContract.*
import com.yapp.presentation.ui.member.qrcodescan.QrCodeScan
import com.yapp.presentation.ui.member.todaysession.TodaySession
import kotlinx.coroutines.flow.collect
import java.lang.reflect.Member

@Composable
fun MemberMain(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    viewModel: MemberMainViewModel = hiltViewModel(),
    navigateToHelpScreen: () -> Unit
) {

    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            BottomNavigationTab(selectedTab = uiState.value.selectedTab)
        }
    ) { innerPadding ->

        LaunchedEffect(key1 = viewModel.effect) {
            viewModel.effect.collect {}
        }

        val modifier = Modifier.padding(innerPadding)

        when (uiState.value.selectedTab) {
            MemberMainNavigationItem.SESSION -> {
                TodaySession(modifier)
            }
            MemberMainNavigationItem.QR_AUTH -> {
                // QR Screen 추가하는 곳
                QrCodeScan(modifier)
            }
            MemberMainNavigationItem.ATTENDANCE -> {
                MemberScore(modifier = modifier) {
                    navigateToHelpScreen()
                }
            }
        }
    }
}

@Composable
fun BottomNavigationTab(
    viewModel: MemberMainViewModel = hiltViewModel(),
    selectedTab: MemberMainNavigationItem
) {
    BottomNavigation(
        backgroundColor = Color.White,
        modifier = Modifier.height(80.dp)
    ) {
        MemberMainNavigationItem.values().forEach { tab ->
            BottomNavigationItem(
                icon = {
                    tab.icon?.let {
                        Icon(
                            painterResource(id = it),
                            contentDescription = null,
                            tint = Color.Unspecified,
                        )
                    }
                },
                label = if (tab.title != null) {
                    {
                        Text(
                            text = stringResource(tab.title),
                            color = Gray_600,
                            style = AttendanceTypography.caption
                        )
                    }
                } else null,
                selected = tab == selectedTab,
                onClick = {
                    viewModel.setEvent(MemberMainUiEvent.OnClickBottomNavigationTab(tab))
                },
                selectedContentColor = Gray_600,
                unselectedContentColor = Gray_1000,
            )
        }
    }
}

enum class MemberMainNavigationItem(val icon: Int?, @StringRes val title: Int?) {
    SESSION(null, R.string.member_main_bottom_navigation_todays_session_text),
    QR_AUTH(R.drawable.icon_camera, null),
    ATTENDANCE(null, R.string.member_main_bottom_navigation_attendance_detail_text);
}
