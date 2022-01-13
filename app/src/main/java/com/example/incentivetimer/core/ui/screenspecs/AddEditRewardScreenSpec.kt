package com.example.incentivetimer.core.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.example.incentivetimer.AddEditReward.AddEditRewardScreen
import com.example.incentivetimer.R
import com.example.incentivetimer.application.ARG_HIDE_BOTTOM_BAR

object AddEditRewardScreenSpec : ScreenSpec {
    override val navHostRoute: String = "add_edit_reward?$ARG_REWARD_ID={$ARG_REWARD_ID}"

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(ARG_REWARD_ID) {
                defaultValue = NO_REWARD_ID
            },
            navArgument(ARG_HIDE_BOTTOM_BAR) {
                defaultValue = true
            }
        )

    fun isEditMode(rewardId: Long?) = rewardId != NO_REWARD_ID

    fun buildRoute(rewardId: Long = NO_REWARD_ID) = "add_edit_reward?$ARG_REWARD_ID=$rewardId"

    fun getRewardIdFromSavedStateHandle(savedStateHandle: SavedStateHandle) =
        savedStateHandle.get<Long>(ARG_REWARD_ID)

    override fun getScreenTitle(navBackStackEntry: NavBackStackEntry?): Int {
        val rewardId = navBackStackEntry?.arguments?.getLong(ARG_REWARD_ID)
        return if(isEditMode(rewardId)) R.string.edit_reward else R.string.add_reward
    }

    @Composable
    override fun Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        AddEditRewardScreen(navController = navController)
    }
}

private const val ARG_REWARD_ID = "rewardId"
const val NO_REWARD_ID = -1L