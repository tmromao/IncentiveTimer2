package com.example.incentivetimer.AddEditReward

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.incentivetimer.R

import com.example.incentivetimer.ui.theme.IncentiveTimerTheme

@Composable
fun AddEditRewardScreen() {
    val viewModel: AddEditResourceViewModel = hiltViewModel()
    val rewardNameInput by viewModel.rewardNameInput.observeAsState()
    val isEditModel = viewModel.isEditMode

    ScreenContent(
        isEditMode = isEditModel,
        rewardNameInput = rewardNameInput,
        onRewardNameInputChanged = viewModel::onRewardNameInputChanged
    )
}

@Composable
private fun ScreenContent(
    isEditMode: Boolean,
    rewardNameInput: String?,
    onRewardNameInputChanged: (input: String) -> Unit,
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
                value = rewardNameInput ?: "",
                onValueChange = onRewardNameInputChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.reward_name)) },
                singleLine = true
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
                onRewardNameInputChanged = {})
        }
    }
}