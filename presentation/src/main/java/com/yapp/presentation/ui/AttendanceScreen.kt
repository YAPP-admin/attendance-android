package com.yapp.presentation.ui

import android.bluetooth.BluetoothDevice
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yapp.common.theme.Yapp_Orange
import com.yapp.common.util.KakaoTalkLoginProvider
import com.yapp.presentation.model.Session
import com.yapp.presentation.ui.admin.browse.AdminTotalScore
import com.yapp.presentation.ui.admin.main.AdminMain
import com.yapp.presentation.ui.login.Login
import com.yapp.presentation.ui.member.help.Help
import com.yapp.presentation.ui.member.main.MemberMain
import com.yapp.presentation.ui.member.qrcodescanner.QrCodeScanner
import com.yapp.presentation.ui.member.score.detail.SessionDetail
import com.yapp.presentation.ui.member.score.detail.SessionDetailNavParam
import com.yapp.presentation.ui.member.setting.MemberSetting
import com.yapp.presentation.ui.member.signup.Name
import com.yapp.presentation.ui.member.signup.Team
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
                kakaoTalkLoginProvider = kakaoTalkLoginProvider,
                navigateToQRMainScreen = {
                    navController.navigate(AttendanceScreenRoute.SIGNUP_NAME.route) {
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
            AdminMain {
                navController.navigate(AttendanceScreenRoute.ADMIN_TOTAL_SCORE.route)
            }
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
        ) {
            QrCodeScanner {
                navController.popBackStack()
            }
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
                        // 모든 스택을 다 제거해야함.
                        popUpTo(AttendanceScreenRoute.MEMBER_SETTING.route)
                    }
                }
            )
        }

        composable(
            route = AttendanceScreenRoute.HELP.route
        ) {
            Help()
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
                onClickNextBtn = {
                    navController.navigate(AttendanceScreenRoute.SIGNUP_TEAM.route)
                })
        }

        composable(
            route = AttendanceScreenRoute.SIGNUP_TEAM.route
        ) {
            Team(navigateToMainScreen = {
                navController.navigate(AttendanceScreenRoute.MEMBER_MAIN.route) {
                    popUpTo(AttendanceScreenRoute.SIGNUP_NAME.route)
                    popUpTo(AttendanceScreenRoute.SIGNUP_TEAM.route)
                }
            })
        }

        composable(
            route = AttendanceScreenRoute.ADMIN_TOTAL_SCORE.route
        ) {
            AdminTotalScore(onClickBackButton = { navController.popBackStack() })
        }
    }
}

enum class AttendanceScreenRoute(val route: String) {
    SPLASH("splash"),
    LOGIN("login"),
    QR_AUTH("qr-auth"),
    MEMBER_MAIN("member-main"),
    ADMIN_MAIN("admin-main"),
    MEMBER_SETTING("member-setting"),
    SIGNUP_NAME("name"),
    SIGNUP_TEAM("team"),
    HELP("help"),
    ADMIN_TOTAL_SCORE("admin-total-score"),
    SESSION_DETAIL("session-detail");
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