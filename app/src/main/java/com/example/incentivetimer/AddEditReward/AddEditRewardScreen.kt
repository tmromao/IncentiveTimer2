package com.example.incentivetimer.AddEditReward

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.incentivetimer.R
import com.example.incentivetimer.core.ui.composables.ITIconButton
import com.example.incentivetimer.ui.IconKey
import com.example.incentivetimer.ui.defaultRewardIconKey

import com.example.incentivetimer.ui.theme.IncentiveTimerTheme
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import kotlinx.coroutines.flow.collect


@Composable
fun AddEditRewardScreen(
    navController: NavController,
) {
    val viewModel: AddEditRewardViewModel = hiltViewModel()
    val isEditMode = viewModel.isEditMode
    val rewardNameInput by viewModel.rewardNameInput.observeAsState("")
    val changeInPercentInput by viewModel.chanceInPercentInput.observeAsState(10)
    val rewardIconKeySelection by viewModel.rewardIconKeySelection.observeAsState(initial = defaultRewardIconKey)
    val showRewardIconSelectionDialog by viewModel.showRewardIconSelectionDialog.observeAsState(
        false
    )

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AddEditRewardViewModel.AddEditRewardEvent.RewardCreated -> navController.popBackStack()
            }
        }
    }

    ScreenContent(
        isEditMode = isEditMode,
        rewardNameInput = rewardNameInput,
        onRewardNameInputChanged = viewModel::onRewardNameInputChanged,
        chanceInPercentInput = changeInPercentInput,
        onChanceInputChanged = viewModel::onChangeInPercentInputChanged,
        rewardIconKeySelection = rewardIconKeySelection,
        onRewardIconButtonClicked = viewModel::onRewardIconButtonClicked,
        showRewardIconSelectionDialog = showRewardIconSelectionDialog,
        onIconSelected = viewModel::onRewardIconSelected,
        onRewardIconDialogDismissRequest = viewModel::onRewardIconDialogDismissed,
        onCloseClicked = { navController.popBackStack() },
        onSaveClicked = viewModel::onSavedClicked,
    )
}

@Composable
private fun ScreenContent(
    isEditMode: Boolean,
    rewardNameInput: String,
    onRewardNameInputChanged: (input: String) -> Unit,
    chanceInPercentInput: Int,
    onChanceInputChanged: (input: Int) -> Unit,
    rewardIconKeySelection: IconKey,
    //4 eventos para lidar com o AlertDialog
    onRewardIconButtonClicked: () -> Unit,
    showRewardIconSelectionDialog: Boolean,
    onIconSelected: (IconKey) -> Unit,
    onRewardIconDialogDismissRequest: () -> Unit,
    //
    onSaveClicked: () -> Unit,
    onCloseClicked: () -> Unit,


    ) {

    Scaffold(
        topBar = {
            val appBarTitle =
                stringResource(if (isEditMode) R.string.edit_reward else R.string.add_reward)
            TopAppBar(
                title = {
                    Text(appBarTitle)
                },
                navigationIcon = {
                    IconButton(onClick = onCloseClicked) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(R.string.close)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSaveClicked,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = stringResource(R.string.save_reward)
                )

            }
        },

        ) {
        Column(Modifier.padding(16.dp)) {
            TextField(
                value = rewardNameInput,
                onValueChange = onRewardNameInputChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.reward_name)) },
                singleLine = true
            )
            Spacer(Modifier.height(16.dp))
            Text(stringResource(R.string.chance) + ": $chanceInPercentInput")
            Slider(
                value = chanceInPercentInput.toFloat() / 100,
                onValueChange = { chanceAsFloat ->
                    onChanceInputChanged((chanceAsFloat * 100).toInt())
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ITIconButton(onClick = onRewardIconButtonClicked, modifier = Modifier.size(64.dp)) {
                Icon(
                    imageVector = rewardIconKeySelection.rewardIcon,
                    contentDescription = stringResource(R.string.select_icon),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )

            }
        }
    }
    if (showRewardIconSelectionDialog) {
        RewardIconSelectionDialog(
            onDismissRequest = onRewardIconDialogDismissRequest,
            onIconSelected = onIconSelected
        )
    }
}

@Composable
private fun RewardIconSelectionDialog(
    onDismissRequest: () -> Unit,
    onIconSelected: (iconKey: IconKey) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(stringResource(R.string.select_icon))
        },
        text = {
            FlowRow(Modifier.fillMaxWidth(), mainAxisAlignment = MainAxisAlignment.Center) {
                IconKey.values().forEach { iconKey ->
                    IconButton(onClick = {
                        onIconSelected(iconKey)
                        onDismissRequest()
                    }) {
                        Icon(
                            imageVector = iconKey.rewardIcon,
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(8.dp)
                        )
                    }
                }
            }

        },
        buttons = {
            TextButton(
                onClick = onDismissRequest,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Preview(
    name = "Light mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Preview(
    name = "Dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
@Composable
private fun RewardItemPreview() {
    IncentiveTimerTheme() {
        Surface {
            ScreenContent(
                isEditMode = false,
                rewardNameInput = "Example reward",
                onRewardNameInputChanged = {},
                chanceInPercentInput = 10,
                onChanceInputChanged = {},
                onCloseClicked = {},
                onSaveClicked = {},
                onRewardIconButtonClicked = {},
                showRewardIconSelectionDialog = false,
                onIconSelected = {},
                onRewardIconDialogDismissRequest = {},
                rewardIconKeySelection = defaultRewardIconKey,
            )
        }
    }
}