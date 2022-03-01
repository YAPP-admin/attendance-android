package com.yapp.presentation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yapp.common.util.KakaoTalkLoginProvider
import com.yapp.presentation.ui.admin.main.AdminMain
import com.yapp.presentation.ui.login.Login
import com.yapp.presentation.ui.member.main.MemberMain
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
            AdminMain()
        }

        composable(
            route = AttendanceScreenRoute.MEMBER_MAIN.route
        ) {
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
            MemberSetting()
        }

        composable(
            route = AttendanceScreenRoute.HELP.route
        ) {}
    }
}

enum class AttendanceScreenRoute(val route: String) {
    SPLASH("splash"),
    LOGIN("login"),
    QR_AUTH("qr-auth"),
    MEMBER_MAIN("member-main"),
    ADMIN_MAIN("admin-main"),
    MEMBER_SETTING("member_setting"),
    HELP("help");
}