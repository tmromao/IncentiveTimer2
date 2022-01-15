package com.example.incentivetimer.core.ui.screenspecs

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

import com.example.incentivetimer.R
import com.example.incentivetimer.features.AddEditReward.ADD_EDIT_REWARD_RESULT
import com.example.incentivetimer.features.AddEditReward.RESULT_REWARD_ADDED
import com.example.incentivetimer.features.AddEditReward.RESULT_REWARD_DELETE
import com.example.incentivetimer.features.AddEditReward.RESULT_REWARD_UPDATED
import com.example.incentivetimer.rewardlist.RewardListScreenAppBar
import com.example.incentivetimer.rewardlist.RewardListScreenContent
import com.example.incentivetimer.features.rewardlist.RewardListViewModel


object RewardListScreenSpec : ScreenSpec {
    override val navHostRoute: String = "rewardList"

    @Composable
    override fun TopBar(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        RewardListScreenAppBar()
    }

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        val viewModel: RewardListViewModel = hiltViewModel(navBackStackEntry)
        val rewards by viewModel.rewards.observeAsState(listOf())

        //TODO : 21/12/2021 Check if we can turn the result into a sealed class!
        val addEditRewardResult =
            navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
                ADD_EDIT_REWARD_RESULT
            )?.observeAsState()

        //ScaffoldState to display the snackbar. Notice it is "=" (not "by")
        val scaffoldState = rememberScaffoldState()

        val context = LocalContext.current

        LaunchedEffect(key1 = addEditRewardResult) {
            // logcat { "LaunchedEffect called" }
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>(
                ADD_EDIT_REWARD_RESULT
            )

            addEditRewardResult?.value?.let { addEditRewardResult ->
                when (addEditRewardResult) {
                    RESULT_REWARD_ADDED -> {
                        scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.reward_added))
                    }
                    RESULT_REWARD_UPDATED -> {
                        scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.reward_updated))
                    }
                    RESULT_REWARD_DELETE -> {
                        scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.reward_deleted))

                    }
                }

            }
        }

        RewardListScreenContent(
            rewards = rewards,
            onAddNewRewardClicked = { navController.navigate(AddEditRewardScreenSpec.buildRoute()) },
            onRewardItemClicked = { id ->
                navController.navigate(AddEditRewardScreenSpec.buildRoute(id))
            },
            scaffoldState = scaffoldState
        )
    }

}