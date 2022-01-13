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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.incentivetimer.AddEditReward.AddEditRewardScreen
import com.example.incentivetimer.R
import com.example.incentivetimer.core.ui.screenspecs.AddEditRewardScreenSpec
import com.example.incentivetimer.core.ui.screenspecs.RewardListScreenSpec
import com.example.incentivetimer.core.ui.screenspecs.ScreenSpec
import com.example.incentivetimer.core.ui.screenspecs.TimerScreenSpec
import com.example.incentivetimer.rewardlist.RewardListScreen
import com.example.incentivetimer.timer.TimerScreen
import com.example.incentivetimer.core.ui.theme.IncentiveTimerTheme

import dagger.hilt.android.AndroidEntryPoint
import logcat.logcat

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
    //var bottomBarHeight by remember { mutableStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val appBarTitle = ScreenSpec.allScreens[currentDestination?.route]?.getScreenTitle(navBackStackEntry)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(appBarTitle ?: R.string.app_name))
                }
            )
        },
        bottomBar = {
            val hideBottomBar = navBackStackEntry?.arguments?.getBoolean(ARG_HIDE_BOTTOM_BAR)

            if (hideBottomBar == null || !hideBottomBar) {
                BottomNavigation {
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
                            selected = currentDestination?.hierarchy?.any { it.route == bottomNavDestination.screenSpec.navHostRoute } == true,
                            onClick = {
                                navController.navigate(bottomNavDestination.screenSpec.navHostRoute) {
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
            }
        },

        ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = bottomNavDestinations[0].screenSpec.navHostRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            ScreenSpec.allScreens.values.forEach { screen ->
                composable(
                    route = screen.navHostRoute,
                    arguments = screen.arguments,
                    deepLinks = screen.deepLinks,
                ) { navBackStackEntry ->
                    screen.Content(
                        navController = navController,
                        navBackStackEntry = navBackStackEntry
                    )
                }
            }
        }
    }
}

val bottomNavDestinations = listOf<BottomNavDestination>(
    BottomNavDestination.TimerScreen,
    BottomNavDestination.RewardListScreen
)

sealed class BottomNavDestination(
    val screenSpec: ScreenSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    object TimerScreen :
        BottomNavDestination(screenSpec = TimerScreenSpec, Icons.Outlined.Timer, R.string.timer)

    object RewardListScreen :
        BottomNavDestination(
            screenSpec = RewardListScreenSpec,
            Icons.Outlined.List,
            R.string.reward_list
        )
}

/*sealed class FullScreenDestinations(
    val screenSpec: ScreenSpec,
) {
    object AddEditRewardScreen : FullScreenDestinations(screenSpec = AddEditRewardScreenSpec)
}*/

const val ARG_HIDE_BOTTOM_BAR = "ARG_HIDE_BOTTOM_BAR"

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun ScreenContentPreview() {
    IncentiveTimerTheme {
        ScreenContent()
    }
}