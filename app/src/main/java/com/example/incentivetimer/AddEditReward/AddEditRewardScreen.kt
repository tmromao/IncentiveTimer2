package com.example.incentivetimer.AddEditReward

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.incentivetimer.R

import com.example.incentivetimer.ui.theme.IncentiveTimerTheme

@Composable
fun AddEditRewardScreen(
    navController: NavController,
) {
    val viewModel: AddEditResourceViewModel = hiltViewModel()
    val isEditMode = viewModel.isEditMode
    val rewardNameInput by viewModel.rewardNameInput.observeAsState("")
    val changeInPercentInput by viewModel.chanceInPercentInput.observeAsState(10)

    ScreenContent(
        isEditMode = isEditMode,
        rewardNameInput = rewardNameInput,
        onRewardNameInputChanged = viewModel::onRewardNameInputChanged,
        chanceInPercentInput = changeInPercentInput,
        onChanceInputChanged = viewModel::onChangeInPercentInputChanged
    )
}

@Composable
private fun ScreenContent(
    isEditMode: Boolean,
    rewardNameInput: String,
    onRewardNameInputChanged: (input: String) -> Unit,
    chanceInPercentInput: Int,
    onChanceInputChanged: (input: Int) -> Unit,
) {

    Scaffold(
        topBar = {
            val appBarTitle =
                stringResource(if (isEditMode) R.string.edit_reward else R.string.add_reward)
            TopAppBar(title = {
                Text(appBarTitle)
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
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
            Text(
                text = "$chanceInPercentInput%",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
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
            )
        }
    }
}