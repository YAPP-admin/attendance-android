package com.yapp.presentation.ui.member

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yapp.presentation.ui.member.main.screen.Main

@Composable
fun AttendanceScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable(
            route = "main"
        ) {
            Main()
        }
    }
}
