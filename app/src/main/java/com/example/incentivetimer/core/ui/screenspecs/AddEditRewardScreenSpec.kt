package com.example.incentivetimer.core.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.example.incentivetimer.AddEditReward.ARG_REWARD_ID
import com.example.incentivetimer.AddEditReward.AddEditRewardScreen
import com.example.incentivetimer.AddEditReward.NO_REWARD_ID
import com.example.incentivetimer.application.ARG_HIDE_BOTTOM_BAR

object AddEditRewardScreenSpec : ScreenSpec {
    override val route: String = "add_edit_screen"

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(ARG_REWARD_ID) {
                defaultValue = NO_REWARD_ID
            }, navArgument(ARG_HIDE_BOTTOM_BAR) {
                defaultValue = true
            }
        )

    @Composable
    override fun Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        AddEditRewardScreen(navController = navController)
    }
}