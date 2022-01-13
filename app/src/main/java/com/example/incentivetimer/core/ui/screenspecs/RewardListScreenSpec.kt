package com.example.incentivetimer.core.ui.screenspecs

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.incentivetimer.R
import com.example.incentivetimer.rewardlist.RewardListScreen


object RewardListScreenSpec : ScreenSpec {
    override val navHostRoute: String = "rewardList"

    @Composable
    override fun TopBar() {
        TopAppBar(title = {
            Text(stringResource(R.string.reward_list))
        })
    }

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        RewardListScreen(navController = navController)
    }

}