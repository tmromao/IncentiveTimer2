package com.example.incentivetimer.core.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.incentivetimer.timer.TimerScreen

object TimerScreenSpec : ScreenSpec {
    override val route: String = "Timer"


    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        TimerScreen(navController = navController)
    }
}