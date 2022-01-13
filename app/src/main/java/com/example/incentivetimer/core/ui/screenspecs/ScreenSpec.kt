package com.example.incentivetimer.core.ui.screenspecs

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import com.example.incentivetimer.R

sealed interface ScreenSpec {

    companion object {
        val allScreens = listOf<ScreenSpec>(
            TimerScreenSpec,
            RewardListScreenSpec,
            AddEditRewardScreenSpec,
        ).associateBy { it.navHostRoute }
    }

    val navHostRoute: String

   /* @StringRes
    fun getScreenTitle(navBackStackEntry: NavBackStackEntry?): Int*/

    val arguments: List<NamedNavArgument> get() = emptyList()
    val deepLinks: List<NavDeepLink> get() = emptyList()

    @Composable
    fun TopBar()

    @Composable
    fun Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    )
}