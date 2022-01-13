package com.example.incentivetimer.core.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.incentivetimer.AddEditReward.AddEditRewardScreen

object AddEditRewardScreenSpec : ScreenSpec {
    override val route: String = "add_edit_screen"


    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        AddEditRewardScreen(navController = navController)
    }
}