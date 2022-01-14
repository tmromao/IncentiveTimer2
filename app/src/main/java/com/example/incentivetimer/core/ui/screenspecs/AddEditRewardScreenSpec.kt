package com.example.incentivetimer.core.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.example.incentivetimer.AddEditReward.*
import com.example.incentivetimer.R
import com.example.incentivetimer.application.ARG_HIDE_BOTTOM_BAR
import com.example.incentivetimer.core.ui.defaultRewardIconKey
import com.example.incentivetimer.core.util.exhaustive
import kotlinx.coroutines.flow.collect

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

    @Composable
    override fun TopBar(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        val viewModel: AddEditRewardViewModel = hiltViewModel(navBackStackEntry)
        val rewardId = navBackStackEntry.arguments?.getLong(ARG_REWARD_ID)

        AddEditRewardScreenAppBar(
            isEditMode = isEditMode(rewardId),
            onCloseClicked = {
                navController.popBackStack()
            },
            actions = viewModel
        )
    }

    @Composable
    override fun Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        val viewModel: AddEditRewardViewModel = hiltViewModel()
        val isEditMode = viewModel.isEditMode
        val rewardNameInput by viewModel.rewardNameInput.observeAsState("")
        val rewardNameInputIsError by viewModel.rewardNameInputIsError.observeAsState(false)
        val changeInPercentInput by viewModel.chanceInPercentInput.observeAsState(10)
        val rewardIconKeySelection by viewModel.rewardIconKeySelection.observeAsState(initial = defaultRewardIconKey)
        val showRewardIconSelectionDialog by viewModel.showRewardIconSelectionDialog.observeAsState(
            false
        )
        val showDeleteRewardConfirmationDialog by viewModel.showDeleteRewardConfirmationDialog.observeAsState(
            false
        )

        val keyboardController = LocalSoftwareKeyboardController.current

        LaunchedEffect(Unit) {
            viewModel.events.collect { event ->
                when (event) {
                    AddEditRewardViewModel.AddEditRewardEvent.RewardCreated -> {
                        keyboardController?.hide()
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            ADD_EDIT_REWARD_RESULT, RESULT_REWARD_ADDED
                        )
                        navController.popBackStack()
                    }
                    AddEditRewardViewModel.AddEditRewardEvent.RewardUpdated -> {
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            ADD_EDIT_REWARD_RESULT, RESULT_REWARD_UPDATED
                        )
                        navController.popBackStack()
                    }
                    AddEditRewardViewModel.AddEditRewardEvent.RewardDeleted -> {
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            ADD_EDIT_REWARD_RESULT, RESULT_REWARD_DELETE
                        )
                        navController.popBackStack()
                    }
                }.exhaustive
            }
        }

        AddEditRewardScreenContent(
            isEditMode = isEditMode,
            rewardNameInput = rewardNameInput,
            rewardNameInputIsError = rewardNameInputIsError,
            chanceInPercentInput = changeInPercentInput,
            rewardIconKeySelection = rewardIconKeySelection,
            showRewardIconSelectionDialog = showRewardIconSelectionDialog,
            showDeleteRewardConfirmationDialog = showDeleteRewardConfirmationDialog,
            actions = viewModel,
        )
    }
}

private const val ARG_REWARD_ID = "rewardId"
const val NO_REWARD_ID = -1L