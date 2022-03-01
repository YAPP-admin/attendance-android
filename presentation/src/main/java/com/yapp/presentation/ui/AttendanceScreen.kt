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
import com.yapp.presentation.ui.member.signup.Team

@Composable
fun AttendanceScreen(
    kakaoTalkLoginProvider: KakaoTalkLoginProvider,
    isLoggedIn: Boolean,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
//        startDestination = if (isLoggedIn) "member_main" else "login"
        startDestination = "team"
    ) {
        composable(
            route = "login"
        ) {
            Login(
                kakaoTalkLoginProvider
            ) {
                navController.navigate("member_main") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }

        composable(
            route = "admin_main"
        ) {
            AdminMain()
        }

        composable(
            route = "member_main"
        ) {
            MemberMain {
                navController.navigate("help")
            }
        }

        composable(
            route = "help"
        ) {}

        composable(
            route = "team"
        ) {
            Team(navigateToMainScreen = { navController.navigate("member_main") })
        }
    }
}
