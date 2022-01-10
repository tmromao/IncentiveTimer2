package com.example.incentivetimer.rewardlist

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.incentivetimer.AddEditReward.ARG_REWARD_ID
import com.example.incentivetimer.R
import com.example.incentivetimer.application.FullScreenDestinations
import com.example.incentivetimer.data.Reward
import com.example.incentivetimer.ui.IconKey
import com.example.incentivetimer.ui.theme.IncentiveTimerTheme
import com.example.incentivetimer.ui.theme.ListBottomPadding
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun RewardListScreen(
    navController: NavController,
    viewModel: RewardListViewModel = hiltViewModel()
) {
    val rewards by viewModel.rewards.observeAsState(listOf())
    //val dummyRewards by viewModel.dummyRewards.observeAsState(listOf())
    //ScreenContent(dummyRewards)
    ScreenContent(rewards = rewards,
        onAddNewRewardClicked = { navController.navigate(FullScreenDestinations.AddEditRewardScreen.route) },
        onRewardItemClicked = { id ->
            navController.navigate(FullScreenDestinations.AddEditRewardScreen.route + "?$ARG_REWARD_ID=$id")
        })
}

@ExperimentalAnimationApi
@Composable
private fun ScreenContent(
    rewards: List<Reward>,
    onRewardItemClicked: (Long) -> Unit,
    onAddNewRewardClicked: () -> Unit,
) {

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
            FloatingActionButton(
                onClick = onAddNewRewardClicked,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_new_reward)
                )

            }
        }
    ) {
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        Box(
            /* contentAlignment = Alignment.Center,
             modifier = Modifier
                 .fillMaxSize()
                 .padding(8.dp)*/
        ) {
            // Text(stringResource(R.string.reward_list))
            LazyColumn(
                contentPadding = PaddingValues(
                    top = 8.dp,
                    start = 8.dp,
                    end = 8.dp,
                    bottom = ListBottomPadding
                ),
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(rewards) { reward ->
                    RewardItem(reward = reward, onItemClicked = { id ->
                        onRewardItemClicked(id)
                    })
                }
            }
            AnimatedVisibility(
                visible = listState.firstVisibleItemIndex > 5,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    backgroundColor = Color.LightGray,
                    contentColor = Color.Black,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExpandLess,
                        contentDescription = stringResource(R.string.string_to_top),

                        )
                }
            }
        }
    }

}


@Composable
private fun RewardItem(
    reward: Reward,
    onItemClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onItemClicked(reward.id) },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = reward.iconKey.rewardIcon,
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(48.dp)
                    .fillMaxWidth()
            )
            Column() {
                Text(
                    text = reward.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = stringResource(R.string.chance) + ":${reward.changeInPercent}%",
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
            RewardItem(Reward(IconKey.BATH_TUB, "Title", 5), onItemClicked = {})
        }
    }
}

@ExperimentalAnimationApi
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
                    Reward(iconKey = IconKey.CAKE, name = "CAKE", 5),
                    Reward(iconKey = IconKey.BATH_TUB, name = "BATH_TUB", 15),
                    Reward(iconKey = IconKey.TV, name = "TV", 25),
                ),
                onAddNewRewardClicked = {},
                onRewardItemClicked = {}

            )

        }
    }
}

