package com.yapp.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yapp.common.theme.Yapp_Orange
import com.yapp.common.yds.YDSToast
import com.yapp.presentation.ui.admin.AdminConstants.KEY_SESSION_ID
import com.yapp.presentation.ui.admin.AdminConstants.KEY_SESSION_TITLE
import com.yapp.presentation.ui.admin.main.AdminMain
import com.yapp.presentation.ui.admin.management.AttendanceManagement
import com.yapp.presentation.ui.admin.totalscore.AdminTotalScore
import com.yapp.presentation.ui.login.Login
import com.yapp.presentation.ui.member.help.Help
import com.yapp.presentation.ui.member.main.MemberMain
import com.yapp.presentation.ui.member.privacyPolicy.PrivacyPolicyScreen
import com.yapp.presentation.ui.member.qrcodescanner.QrCodeScanner
import com.yapp.presentation.ui.member.score.detail.SessionDetail
import com.yapp.presentation.ui.member.score.detail.SessionDetailNavParam
import com.yapp.presentation.ui.member.setting.MemberSetting
import com.yapp.presentation.ui.member.signup.name.Name
import com.yapp.presentation.ui.member.signup.position.Position
import com.yapp.presentation.ui.member.signup.team.Team
import com.yapp.presentation.ui.splash.Splash
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AttendanceScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    var qrToastVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MainContract.MainUiSideEffect.NavigateToQRScreen -> {
                    navController.navigate(AttendanceScreenRoute.QR_AUTH.route)
                }
                is MainContract.MainUiSideEffect.ShowToast -> {
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
                    navController.navigate(AttendanceScreenRoute.SIGNUP_NAME.route) {
                        popUpTo(AttendanceScreenRoute.LOGIN.route) { inclusive = true }
                    }
                })
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
                        viewModel.setEvent(MainContract.MainUiEvent.OnClickQrAuthButton)
                    } else {
                        navController.navigate(route)
                    }
                }
            )
        }

        composable(
            route = AttendanceScreenRoute.QR_AUTH.route
        ) {
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
            AttendanceManagement(
                onBackButtonClicked = { navController.popBackStack() }
            )
        }

        composable(
            route = AttendanceScreenRoute.MEMBER_SETTING.route
        ) {
            MemberSetting(
                onClickBackButton = {
                    navController.popBackStack()
                },
                onClickAdminButton = {
                    navController.navigate(AttendanceScreenRoute.ADMIN_MAIN.route) {
                        popUpTo(AttendanceScreenRoute.MEMBER_SETTING.route) { inclusive = true }
                    }
                },
                onClickLogoutButton = {
                    navController.navigate(AttendanceScreenRoute.LOGIN.route) {
                        // TODO: 모든 스택을 다 제거해야함.
                        popUpTo(AttendanceScreenRoute.MEMBER_SETTING.route)
                    }
                },
                onClickPrivacyPolicyButton = {
                    navController.navigate(AttendanceScreenRoute.PRIVACY_POLICY.route)
                }
            )
        }

        composable(
            route = AttendanceScreenRoute.PRIVACY_POLICY.route
        ) {
            PrivacyPolicyScreen(
                onClickBackButton = { navController.popBackStack() }
            )
        }

        composable(
            route = AttendanceScreenRoute.HELP.route
        ) {
            Help { navController.popBackStack() }
        }

        composable(
            route = AttendanceScreenRoute.SESSION_DETAIL.route
                .plus("?title={title}")
                .plus("?description={description}")
                .plus("?attendanceType={attendanceType}")
                .plus("?date={date}"),
            arguments = listOf(
                navArgument("title") {
                    nullable = false
                    type = NavType.StringType
                },
                navArgument("description") {
                    nullable = false
                    type = NavType.StringType
                },
                navArgument("attendanceType") {
                    nullable = false
                    type = NavType.StringType
                },
                navArgument("date") {
                    nullable = false
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            SessionDetail(
                navParam = SessionDetailNavParam(
                    title = backStackEntry.arguments?.getString("title")!!,
                    description = backStackEntry.arguments?.getString("description")!!,
                    attendanceType = backStackEntry.arguments?.getString("attendanceType")!!,
                    date = backStackEntry.arguments?.getString("date")!!,
                ),
                onClickBackButton = { navController.popBackStack() }
            )
        }

        composable(
            route = AttendanceScreenRoute.SIGNUP_NAME.route
        ) {
            Name(
                onClickBackBtn = { navController.popBackStack() },
                onClickNextBtn = { userName -> navController.navigate(AttendanceScreenRoute.SIGNUP_POSITION.route + "/${userName}") })
        }

        composable(
            route = AttendanceScreenRoute.SIGNUP_POSITION.route
                .plus("/{name}"),
            arguments = listOf(
                navArgument("name") { type = NavType.StringType })
        )
        {
            Position(
                onClickBackButton = { navController.popBackStack() },
                navigateToTeamScreen = { userName, userPosition ->
                    navController.navigate(
                        AttendanceScreenRoute.SIGNUP_TEAM.route.plus("/${userName}")
                            .plus("/${userPosition}")
                    )
                }
            )
        }


        composable(
            route = AttendanceScreenRoute.SIGNUP_TEAM.route
                .plus("/{name}")
                .plus("/{position}"),
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("position") { type = NavType.StringType })
        ) {
            Team(
                onClickBackButton = { navController.popBackStack() },
                navigateToMainScreen = {
                    navController.navigate(AttendanceScreenRoute.MEMBER_MAIN.route) {
                        popUpTo(AttendanceScreenRoute.SIGNUP_TEAM.route)
                    }
                })
        }

        composable(
            route = AttendanceScreenRoute.ADMIN_TOTAL_SCORE.route
                .plus("/{$KEY_UPCOMING_SESSION_ID}"),
            arguments = listOf(
                navArgument(KEY_UPCOMING_SESSION_ID) { type = NavType.IntType }
            )
        ) {
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
                .padding(bottom = 90.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            YDSToast(
                text = stringResource(id = com.yapp.presentation.R.string.member_main_qr_enter_failed_toast_message),
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
    HELP("help"),
    ADMIN_TOTAL_SCORE("admin-total-score"),
    SESSION_DETAIL("session-detail"),
    PRIVACY_POLICY("privacy-policy"),
    ADMIN_ATTENDANCE_MANAGEMENT("admin-attendance-management");
}

// status bar color 한번에 지정할 수 있는 방법 찾아보기 !
@Composable
private fun SetStatusBarColorByRoute(route: String?) {
    val systemUiController = rememberSystemUiController()

    when (route) {
        AttendanceScreenRoute.SPLASH.route -> {
            systemUiController.setSystemBarsColor(
                color = Yapp_Orange
            )
        }
        else -> {
            systemUiController.setSystemBarsColor(
                color = Color.White
            )
        }
    }
}

const val KEY_UPCOMING_SESSION_ID = "upcomingSessionId"