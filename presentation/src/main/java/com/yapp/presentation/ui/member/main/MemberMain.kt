package com.yapp.presentation.ui.member.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yapp.common.theme.*
import com.yapp.presentation.R
import com.yapp.presentation.ui.AttendanceScreenRoute
import com.yapp.presentation.ui.member.detail.MemberScore
import com.yapp.presentation.ui.member.todaysession.TodaySession
import kotlinx.coroutines.flow.collect

@Composable
fun MemberMain(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    viewModel: MemberMainViewModel = hiltViewModel(),
    navigateToScreen: (String) -> Unit,
) {
    val childNavController = rememberNavController()

    AttendanceTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier
                .fillMaxSize(),
            bottomBar = {
                BottomNavigationTab(
                    navController = childNavController,
                    navigateToScreen = navigateToScreen
                )
            }
        ) { innerPadding ->

            LaunchedEffect(key1 = viewModel.effect) {
                viewModel.effect.collect {}
            }

            val modifier = Modifier.padding(innerPadding)

            ChildNavigation(
                childNavController,
                modifier
            )
        }
    }
}

@Composable
fun BottomNavigationTab(
    navController: NavController,
    navigateToScreen: (String) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation(
        backgroundColor = Color.White,
        modifier = Modifier.height(80.dp),
        elevation = 2.dp
    ) {
        BottomNavigationItem.values().forEach { tab ->
            BottomNavigationItem(
                modifier = Modifier.padding(8.dp),
                icon = {
                    Icon(
                        painterResource(id = tab.icon),
                        contentDescription = null,
                        modifier = Modifier.padding(4.dp),
                        tint = Color.Unspecified,
                    )
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
                selected = tab.route == currentRoute,
                onClick = {
                    if (tab.route == AttendanceScreenRoute.QR_AUTH.route) {
                        navigateToScreen(AttendanceScreenRoute.QR_AUTH.route)
                    } else {
                        navController.navigate(tab.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                selectedContentColor = Gray_600,
                unselectedContentColor = Gray_1000,
            )
        }
    }
}

enum class BottomNavigationItem(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val title: Int?
) {
    SESSION(
        "today-session",
        R.drawable.icon_home_enabled,
        R.string.member_main_bottom_navigation_today_session_text
    ),
    QR_AUTH("qr-auth", R.drawable.icon_camera, null),
    MEMBER_SCORE(
        "member-score",
        R.drawable.icon_check_disabled,
        R.string.member_main_bottom_navigation_attendance_detail_text
    );
}


@Composable
private fun ChildNavigation(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(navController, startDestination = BottomNavigationItem.SESSION.route) {
        composable(
            route = BottomNavigationItem.SESSION.route
        ) {
            TodaySession(
                modifier = modifier
            )
        }

        composable(
            route = BottomNavigationItem.MEMBER_SCORE.route
        ) {
            MemberScore(modifier = modifier) {
            }
        }
    }
}