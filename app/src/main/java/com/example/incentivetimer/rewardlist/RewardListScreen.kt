package com.example.incentivetimer.rewardlist

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.incentivetimer.R
import com.example.incentivetimer.data.Reward
import com.example.incentivetimer.ui.theme.IncentiveTimerTheme

@Composable
fun RewardListScreen() {
    ScreenContent()
}

@Composable
private fun ScreenContent() {

    val dummyRewards = mutableListOf<Reward>()
    repeat(12) { index ->
        dummyRewards += Reward(icon = Icons.Default.Star, title = "Item $index", index)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Text(stringResource(R.string.reward_list))
        LazyColumn() {
            items(dummyRewards) { rewardItem ->
                RewardItem(reward = rewardItem)
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
            .padding()
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
            ScreenContent()
        }
    }
}

