package com.yapp.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yapp.common.theme.Yapp_Orange
import com.yapp.common.util.KakaoTalkLoginProvider
import com.yapp.presentation.ui.admin.main.AdminMain
import com.yapp.presentation.ui.login.Login
import com.yapp.presentation.ui.member.main.MemberMain
import com.yapp.presentation.ui.member.signup.Team
import com.yapp.presentation.ui.member.setting.MemberSetting
import com.yapp.presentation.ui.splash.Splash

@Composable
fun AttendanceScreen(
    kakaoTalkLoginProvider: KakaoTalkLoginProvider,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = AttendanceScreenRoute.SPLASH.route

    ) {
        composable(
            route = AttendanceScreenRoute.LOGIN.route
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            Login(
                kakaoTalkLoginProvider
            ) {
                navController.navigate(AttendanceScreenRoute.MEMBER_MAIN.route) {
                    popUpTo(AttendanceScreenRoute.LOGIN.route) { inclusive = true }
                }
            }
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
            AdminMain()
        }

        composable(
            route = AttendanceScreenRoute.MEMBER_MAIN.route
        ) {
            SetStatusBarColorByRoute(it.destination.route)
            MemberMain {
                navController.navigate(it)
            }
        }

        composable(
            route = AttendanceScreenRoute.QR_AUTH.route
        ) {}

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
                })
        }

        composable(
            route = AttendanceScreenRoute.HELP.route
        ) {}

        composable(
            route = AttendanceScreenRoute.SIGNUP_TEAM.route
        ) {
            Team(navigateToMainScreen = { navController.navigate(AttendanceScreenRoute.MEMBER_MAIN.route) })
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
    SIGNUP_TEAM("team"),
    HELP("help");
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