package com.yapp.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.yds.YDSToast
import com.yapp.presentation.ui.MainContract.MainUiEvent
import com.yapp.presentation.ui.MainContract.MainUiSideEffect
import com.yapp.presentation.ui.admin.AdminConstants.KEY_LAST_SESSION_ID
import com.yapp.presentation.ui.admin.AdminConstants.KEY_SESSION_ID
import com.yapp.presentation.ui.admin.AdminConstants.KEY_SESSION_TITLE
import com.yapp.presentation.ui.admin.main.AdminMain
import com.yapp.presentation.ui.admin.management.AttendanceManagement
import com.yapp.presentation.ui.admin.totalscore.AdminTotalScore
import com.yapp.presentation.ui.login.Login
import com.yapp.presentation.ui.member.help.Help
import com.yapp.presentation.ui.member.main.BottomNavigationItem
import com.yapp.presentation.ui.member.main.MemberMain
import com.yapp.presentation.ui.member.privacyPolicy.PrivacyPolicyScreen
import com.yapp.presentation.ui.member.qrcodescanner.QrCodeScanner
import com.yapp.presentation.ui.member.score.detail.SessionDetail
import com.yapp.presentation.ui.member.setting.MemberSetting
import com.yapp.presentation.ui.member.signup.name.Name
import com.yapp.presentation.ui.member.signup.password.Password
import com.yapp.presentation.ui.member.signup.position.Position
import com.yapp.presentation.ui.member.signup.team.Team
import com.yapp.presentation.ui.splash.Splash
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AttendanceScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    var qrToastVisible by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MainUiSideEffect.NavigateToQRScreen -> {
                    navController.navigate(BottomNavigationItem.QR_AUTH.route)
                }

                is MainUiSideEffect.ShowToast -> {
                    qrToastVisible = !qrToastVisible
                    delay(1000L)
                    qrToastVisible = !qrToastVisible
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = AttendanceScreenRoute.SPLASH.route
    ) {
        composable(
            route = AttendanceScreenRoute.LOGIN.route
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            Login(
                navigateToQRMainScreen = {
                    navController.navigate(AttendanceScreenRoute.MEMBER_MAIN.route) {
                        popUpTo(AttendanceScreenRoute.LOGIN.route) { inclusive = true }
                    }
                },
                navigateToSignUpScreen = {
                    navController.navigate(AttendanceScreenRoute.SIGNUP_PASSWORD.route) {
                        popUpTo(AttendanceScreenRoute.LOGIN.route) { inclusive = true }
                    }
                },
                navigateToAdminScreen = {
                    navController.navigate(AttendanceScreenRoute.ADMIN_MAIN.route) {
                        popUpTo(AttendanceScreenRoute.LOGIN.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = AttendanceScreenRoute.SPLASH.route
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            Splash(
                navigateToLogin = {
                    navController.navigate(AttendanceScreenRoute.LOGIN.route) {
                        popUpTo(AttendanceScreenRoute.SPLASH.route) { inclusive = true }
                    }
                },
                navigateToMain = {
                    navController.navigate(AttendanceScreenRoute.MEMBER_MAIN.route) {
                        popUpTo(AttendanceScreenRoute.SPLASH.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = AttendanceScreenRoute.ADMIN_MAIN.route
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            AdminMain(
                navigateToAdminTotalScore = { upcomingSessionId ->
                    navController.navigate(
                        AttendanceScreenRoute.ADMIN_TOTAL_SCORE.route
                            .plus("/${upcomingSessionId}")
                    )
                },
                navigateToManagement = { sessionId, sessionTitle ->
                    navController.navigate(
                        AttendanceScreenRoute.ADMIN_ATTENDANCE_MANAGEMENT.route
                            .plus("/${sessionId}")
                            .plus("/${sessionTitle}")
                    )
                },
                navigateToLogin = {
                    navController.navigate(
                        AttendanceScreenRoute.LOGIN.route
                    ) {
                        popUpTo(AttendanceScreenRoute.ADMIN_MAIN.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = AttendanceScreenRoute.MEMBER_MAIN.route
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            MemberMain(
                navigateToScreen = { route ->
                    if (route == AttendanceScreenRoute.QR_AUTH.route) {
                        viewModel.setEvent(MainUiEvent.OnClickQrAuthButton)
                    } else {
                        navController.navigate(route)
                    }
                }
            )
        }

        composable(
            route = AttendanceScreenRoute.QR_AUTH.route
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            QrCodeScanner {
                navController.popBackStack()
            }
        }

        composable(
            route = AttendanceScreenRoute.ADMIN_ATTENDANCE_MANAGEMENT.route
                .plus("/{$KEY_SESSION_ID}")
                .plus("/{$KEY_SESSION_TITLE}"),
            arguments = listOf(
                navArgument(KEY_SESSION_ID) { type = NavType.IntType },
                navArgument(KEY_SESSION_TITLE) { type = NavType.StringType }
            )
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            AttendanceManagement(
                onBackButtonClicked = { navController.popBackStack() }
            )
        }

        composable(
            route = AttendanceScreenRoute.MEMBER_SETTING.route
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            MemberSetting(
                navigateToPreviousScreen = {
                    navController.popBackStack()
                },
                navigateToLogin = {
                    navController.navigate(AttendanceScreenRoute.LOGIN.route) {
                        popUpTo(0)
                    }
                },
                navigateToPrivacyPolicy = {
                    navController.navigate(AttendanceScreenRoute.PRIVACY_POLICY.route)
                },
                navigateToSelectTeamScreen = {
                    navController.navigate(AttendanceScreenRoute.SIGNUP_TEAM.route) {
                        popUpTo(AttendanceScreenRoute.MEMBER_SETTING.route) { inclusive = true }
                    }
                },
            )
        }

        composable(
            route = AttendanceScreenRoute.PRIVACY_POLICY.route
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            PrivacyPolicyScreen(
                onClickBackButton = { navController.popBackStack() }
            )
        }

        composable(
            route = AttendanceScreenRoute.HELP.route
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            Help { navController.popBackStack() }
        }

        composable(
            route = AttendanceScreenRoute.SESSION_DETAIL.route
                .plus("/{session}"),
            arguments = listOf(
                navArgument("session") {
                    nullable = false
                    type = NavType.IntType
                },
            )
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            SessionDetail { navController.popBackStack() }
        }

        composable(
            route = AttendanceScreenRoute.SIGNUP_PASSWORD.route,
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            Password(
                onClickBackButton = {
                    navController.navigate(AttendanceScreenRoute.LOGIN.route) {
                        popUpTo(AttendanceScreenRoute.SIGNUP_PASSWORD.route) { inclusive = true }
                    }
                },
                onClickNextButton = {
                    navController.navigate(AttendanceScreenRoute.SIGNUP_NAME.route) {
                        popUpTo(AttendanceScreenRoute.SIGNUP_PASSWORD.route) { inclusive = true }
                    }
                })
        }

        composable(
            route = AttendanceScreenRoute.SIGNUP_NAME.route
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            Name(
                onClickBackBtn = {
                    navController.navigate(AttendanceScreenRoute.LOGIN.route) {
                        popUpTo(AttendanceScreenRoute.SIGNUP_NAME.route) { inclusive = true }
                    }
                },
                onClickNextBtn = { userName -> navController.navigate(AttendanceScreenRoute.SIGNUP_POSITION.route + "/${userName}") })
        }

        composable(
            route = AttendanceScreenRoute.SIGNUP_POSITION.route
                .plus("/{name}"),
            arguments = listOf(
                navArgument("name") { type = NavType.StringType })
        )
        {
            SetStatusBarColorByRoute(it.destination.route)
            Position(
                onClickBackButton = { navController.popBackStack() },
                navigateToMainScreen = {
                    navController.navigate(AttendanceScreenRoute.MEMBER_MAIN.route) {
                        popUpTo(AttendanceScreenRoute.SIGNUP_NAME.route) { inclusive = true }
                    }
                }
            )
        }


        composable(
            route = AttendanceScreenRoute.SIGNUP_TEAM.route,
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            Team(
                onClickBackButton = { navController.popBackStack() },
                navigateToSettingScreen = {
                    navController.navigate(AttendanceScreenRoute.MEMBER_SETTING.route) {
                        popUpTo(AttendanceScreenRoute.SIGNUP_TEAM.route) { inclusive = true }
                    }
                })
        }

        composable(
            route = AttendanceScreenRoute.ADMIN_TOTAL_SCORE.route
                .plus("/{$KEY_LAST_SESSION_ID}"),
            arguments = listOf(
                navArgument(KEY_LAST_SESSION_ID) { type = NavType.IntType }
            )
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            AdminTotalScore(navigateToPreviousScreen = { navController.popBackStack() })
        }
    }

    AnimatedVisibility(
        visible = qrToastVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(bottom = 90.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            YDSToast(
                text = uiState.toastMessage,
            )
        }
    }
}

enum class AttendanceScreenRoute(val route: String) {
    SPLASH("splash"),
    LOGIN("login"),
    QR_AUTH("qr-auth"),
    MEMBER_MAIN("member-main"),
    ADMIN_MAIN("admin-main"),
    MEMBER_SETTING("member_setting"),
    SIGNUP_NAME("signup-name"),
    SIGNUP_POSITION("signup-position"),
    SIGNUP_TEAM("signup-team"),
    SIGNUP_PASSWORD("signup-password"),
    HELP("help"),
    ADMIN_TOTAL_SCORE("admin-total-score"),
    SESSION_DETAIL("session-detail"),
    PRIVACY_POLICY("privacy-policy"),
    ADMIN_ATTENDANCE_MANAGEMENT("admin-attendance-management");
}

@Composable
fun SetStatusBarColorByRoute(route: String?) {
    val systemUiController = rememberSystemUiController()

    val shouldShowLightIcon = !isSystemInDarkTheme()
    val yappOrange = AttendanceTheme.colors.mainColors.YappOrange
    val backgroundBase = AttendanceTheme.colors.backgroundColors.backgroundBase

    SideEffect {
        when (route) {
            AttendanceScreenRoute.SPLASH.route -> {
                systemUiController.setStatusBarColor(
                    color = yappOrange,
                )
            }

            BottomNavigationItem.SESSION.route -> {
                systemUiController.setStatusBarColor(
                    color = backgroundBase,
                    darkIcons = shouldShowLightIcon
                )
            }

            else -> {
                systemUiController.setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = shouldShowLightIcon
                )
            }
        }
    }
}