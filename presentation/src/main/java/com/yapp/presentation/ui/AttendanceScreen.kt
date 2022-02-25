package com.yapp.presentation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yapp.common.util.KakaoTalkLoginProvider
import com.yapp.presentation.ui.admin.main.AdminMain
import com.yapp.presentation.ui.login.Login
import com.yapp.presentation.ui.member.detail.MemberScore
import com.yapp.presentation.ui.member.main.MemberMain

@Composable
fun AttendanceScreen(
    kakaoTalkLoginProvider: KakaoTalkLoginProvider,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "login"
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
    }
}
