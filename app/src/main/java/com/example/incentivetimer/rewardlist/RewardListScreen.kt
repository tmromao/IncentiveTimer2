package com.example.incentivetimer.rewardlist

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Deck
import androidx.compose.material.icons.filled.LocalCarWash
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.incentivetimer.R
import com.example.incentivetimer.data.Reward
import com.example.incentivetimer.ui.theme.IncentiveTimerTheme
import com.example.incentivetimer.ui.theme.ListBottomPadding

@Composable
fun RewardListScreen(
    viewModel: RewardListViewModel = hiltViewModel()
) {
    val dummyRewards by viewModel.dummyRewards.observeAsState(listOf())
    ScreenContent(dummyRewards)
}

@Composable
private fun ScreenContent(
    rewards: List<Reward>
)
{

   /* val dummyRewards = mutableListOf<Reward>()
    repeat(12) { index ->
        dummyRewards += Reward(icon = Icons.Default.Star, title = "Item $index", index)
    }*/

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(stringResource(R.string.reward_list))
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_new_reward))

            }
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Text(stringResource(R.string.reward_list))
            LazyColumn(contentPadding = PaddingValues(bottom = ListBottomPadding)) {
                items(rewards) { rewardItem ->
                    RewardItem(reward = rewardItem)
                }
            }
        }
    }

}


@Composable
private fun RewardItem(
    reward: Reward,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = {},
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = reward.icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(48.dp)
                    .fillMaxWidth()
            )
            Column() {
                Text(
                    text = reward.title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = "${reward.changeInPercent}%",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.fillMaxWidth()
                )

            }
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
        Surface() {
            RewardItem(Reward(Icons.Default.Star, "Title", 5))
        }
    }
}

@Preview(
    name = "Light mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Preview(
    name = "Dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun ScreenContentPreview() {
    IncentiveTimerTheme() {
        Surface() {
            ScreenContent(
                listOf(
                    Reward(icon = Icons.Default.Star, title = "Reward 1", 5),
                    Reward(icon = Icons.Default.LocalCarWash, title = "Reward 2", 15),
                    Reward(icon = Icons.Default.Deck, title = "Reward 3", 25),
                )
            )
        }
    }
}

