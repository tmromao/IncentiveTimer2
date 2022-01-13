package com.example.incentivetimer.core.ui.screenspecs

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.incentivetimer.R
import com.example.incentivetimer.timer.TimerScreen

object TimerScreenSpec : ScreenSpec {
    override val navHostRoute: String = "Timer"

    @Composable
    override fun TopBar() {
        TopAppBar(title = {
            Text(stringResource(R.string.timer))
        })
    }

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        TimerScreen(navController = navController)
    }
}