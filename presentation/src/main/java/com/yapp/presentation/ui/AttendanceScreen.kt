package com.yapp.presentation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yapp.presentation.ui.login.Login
import com.yapp.presentation.ui.member.main.Main

@Composable
fun AttendanceScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable(
            route = "login"
        ) {
            Login {
                navController.navigate("main")
            }
        }

        composable(
            route = "main"
        ) {
            Main()
        }
    }
}
