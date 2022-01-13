package com.example.incentivetimer.core.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.incentivetimer.R
import com.example.incentivetimer.rewardlist.RewardListScreen


object RewardListScreenSpec : ScreenSpec {
    override val navHostRoute: String = "rewardList"

    override fun getScreenTitle(navBackStackEntry: NavBackStackEntry?): Int = R.string.reward_list

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        RewardListScreen(navController = navController)
    }

}