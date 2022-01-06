package com.example.incentivetimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.incentivetimer.rewardlist.RewardListScreen
import com.example.incentivetimer.timer.TimerScreen
import com.example.incentivetimer.ui.theme.IncentiveTimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IncentiveTimerTheme {
                ScreenContent()
            }
        }
    }
}


@Composable
private fun ScreenContent() {
    val navController = rememberNavController()



    NavHost(navController = navController, startDestination = "timer") {
        composable(BottomNavDestination.Timer.route) {
            TimerScreen()
        }
        composable(BottomNavDestination.RewardList.route) {
            RewardListScreen()
        }
    }
}

val bottomNavDestinations = listOf<BottomNavDestination>(
    BottomNavDestination.Timer,
    BottomNavDestination.RewardList
)

sealed class BottomNavDestination(
    val route: String,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    object Timer : BottomNavDestination("timer", Icons.Outlined.Timer, R.string.timer)
    object RewardList :
        BottomNavDestination("rewardList", Icons.Outlined.List, R.string.reward_list)
}

@Preview(showBackground = true)
@Composable
fun ScreenContentPreview() {
    IncentiveTimerTheme {
        ScreenContent()
    }
}