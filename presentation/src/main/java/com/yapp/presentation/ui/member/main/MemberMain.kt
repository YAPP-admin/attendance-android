package com.yapp.presentation.ui.member.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.flow.collectAsStateWithLifecycle
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.YDSPopupDialog
import com.yapp.presentation.R
import com.yapp.presentation.ui.AttendanceScreenRoute
import com.yapp.presentation.ui.SetStatusBarColorByRoute
import com.yapp.presentation.ui.member.score.MemberScore
import com.yapp.presentation.ui.member.todaysession.TodaySession


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MemberMain(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    viewModel: MemberMainViewModel = hiltViewModel(),
    navigateToScreen: (String) -> Unit,
    navigateToTeam: () -> Unit
) {
    val childNavController = rememberNavController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AttendanceTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            bottomBar = {
                BottomNavigationTab(
                    navController = childNavController,
                    navigateToScreen = { tab -> viewModel.setEvent(MemberMainContract.MemberMainUiEvent.OnClickBottomNavigationTab(tab)) }
                )
            },
            backgroundColor = AttendanceTheme.colors.backgroundColors.backgroundBase
        ) { innerPadding ->


            if(uiState.isShowTeamDialog) {
                YDSPopupDialog(
                    title = stringResource(id = R.string.member_need_select_team),
                    content = stringResource(id = R.string.member_need_select_team_description),
                    negativeButtonText = stringResource(id = R.string.member_need_select_team_next_time),
                    positiveButtonText = stringResource(id = R.string.member_setting_select_team),
                    onClickNegativeButton = { viewModel.setEvent(MemberMainContract.MemberMainUiEvent.OnNextTime) },
                    onClickPositiveButton = { viewModel.setEvent(MemberMainContract.MemberMainUiEvent.OnSelectTeamScreen) },
                    onDismiss = {}
                )
            }

            LaunchedEffect(key1 = viewModel.effect) {
                viewModel.effect.collect { uiEffect ->
                    when (uiEffect) {
                        is MemberMainContract.MemberMainUiSideEffect.NavigateToRoute -> {
                            navigateBottomNavigationScreen(
                                qrScreenNavigate = navigateToScreen,
                                navController = childNavController,
                                tab = uiEffect.tab
                            )
                        }
                        MemberMainContract.MemberMainUiSideEffect.NavigateToTeam -> {
                            navigateToTeam.invoke()
                        }
                    }
                }
            }

            val modifier = Modifier.padding(innerPadding)

            ChildNavigation(
                childNavController,
                modifier,
                navigateToScreen
            )
        }
    }
}

private fun navigateBottomNavigationScreen(
    qrScreenNavigate: (String) -> Unit,
    navController: NavController,
    tab: BottomNavigationItem
) {
    when (tab) {
        BottomNavigationItem.QR_AUTH -> {
            qrScreenNavigate.invoke(tab.route)
        }
        else -> {
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
    }
}

@Composable
fun BottomNavigationTab(
    navController: NavController,
    navigateToScreen: (BottomNavigationItem) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation(
        backgroundColor = AttendanceTheme.colors.backgroundColors.background,
        modifier = Modifier.height(80.dp),
        elevation = 2.dp
    ) {
        for (navigationTab in BottomNavigationItem.values()) {
            BottomNavigationItem(
                modifier = Modifier.padding(8.dp),
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = navigationTab.icon),
                        contentDescription = null,
                        modifier = Modifier.padding(4.dp),
                        tint = if (navigationTab == BottomNavigationItem.QR_AUTH) Color.Unspecified else LocalContentColor.current,
                    )
                },
                label = if (navigationTab.title != null) {
                    @Composable {
                        Text(
                            text = stringResource(navigationTab.title),
                            color = if (navigationTab.route == currentRoute) AttendanceTheme.colors.grayScale.Gray1000 else AttendanceTheme.colors.grayScale.Gray600,
                            style = AttendanceTypography.caption
                        )
                    }
                } else null,
                selected = navigationTab.route == currentRoute,
                onClick = { navigateToScreen.invoke(navigationTab) },
                selectedContentColor = AttendanceTheme.colors.mainColors.YappOrange,
                unselectedContentColor = AttendanceTheme.colors.grayScale.Gray600,
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
        R.drawable.icon_bottom_navigation_home,
        R.string.member_main_bottom_navigation_today_session_text
    ),
    QR_AUTH("qr-auth", R.drawable.icon_qr, null),
    MEMBER_SCORE(
        "member-score",
        R.drawable.icon_bottom_navigation_check,
        R.string.member_main_bottom_navigation_attendance_detail_text
    );
}


@Composable
private fun ChildNavigation(
    navController: NavHostController,
    modifier: Modifier,
    navigateToScreen: (String) -> Unit
) {
    NavHost(navController, startDestination = BottomNavigationItem.SESSION.route) {
        composable(
            route = BottomNavigationItem.SESSION.route
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            TodaySession(
                modifier = modifier,
                navigateToSetting = navigateToScreen
            )
        }

        composable(
            route = BottomNavigationItem.MEMBER_SCORE.route
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            MemberScore(
                modifier = modifier,
                navigateToHelpScreen = {
                    navigateToScreen(AttendanceScreenRoute.HELP.route)
                },
                navigateToSessionDetail = { sessionId ->
                    navigateToScreen(
                        AttendanceScreenRoute.SESSION_DETAIL.route
                            .plus("/${sessionId}")
                    )
                }
            )
        }
    }
}
