package com.example.incentivetimer.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.incentivetimer.AddEditReward.AddEditRewardScreen
import com.example.incentivetimer.R
import com.example.incentivetimer.rewardlist.RewardListScreen
import com.example.incentivetimer.timer.TimerScreen
import com.example.incentivetimer.ui.theme.IncentiveTimerTheme

import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
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


@ExperimentalAnimationApi
@Composable
private fun ScreenContent() {
    val navController = rememberNavController()

    Scaffold(

        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavDestinations.forEach { bottomNavDestination ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                bottomNavDestination.icon,
                                contentDescription = "null"
                            )
                        },
                        label = {
                            Text(
                                stringResource(bottomNavDestination.label)
                            )
                        },
                        alwaysShowLabel = false,
                        selected = currentDestination?.hierarchy?.any { it.route == bottomNavDestination.route } == true,
                        onClick = {
                            navController.navigate(bottomNavDestination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }//BottomNavigation
        },

        ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = bottomNavDestinations[0].route,
            Modifier.padding(innerPadding),

            ) {
            composable(BottomNavDestination.TimerScreen.route) {
                TimerScreen(navController)
            }
            composable(BottomNavDestination.RewardListScreen.route) {
                RewardListScreen(navController)
            }
            composable(FullScreenDestinations.AddEditRewardScreen.route){
                AddEditRewardScreen(navController)
            }
        }
    }

}

val bottomNavDestinations = listOf<BottomNavDestination>(
    BottomNavDestination.TimerScreen,
    BottomNavDestination.RewardListScreen
)

sealed class BottomNavDestination(
    val route: String,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    object TimerScreen : BottomNavDestination(route = "timer", Icons.Outlined.Timer, R.string.timer)
    object RewardListScreen :
        BottomNavDestination(route = "reward_list", Icons.Outlined.List, R.string.reward_list)
}

sealed class FullScreenDestinations(
    val route: String,
) {
    object AddEditRewardScreen : FullScreenDestinations(route = "add_edit_screen")
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun ScreenContentPreview() {
    IncentiveTimerTheme {
        ScreenContent()
    }
}