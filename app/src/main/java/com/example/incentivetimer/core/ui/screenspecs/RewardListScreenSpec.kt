package com.example.incentivetimer.core.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.incentivetimer.rewardlist.RewardListScreen


object RewardListScreenSpec : ScreenSpec {
    override val route: String = "rewardList"

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        RewardListScreen(navController = navController)
    }
}