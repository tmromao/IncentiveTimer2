package com.example.incentivetimer.core.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.incentivetimer.features.timer.TimerScreenAppBar
import com.example.incentivetimer.features.timer.TimerScreenContent
import com.example.incentivetimer.features.timer.TimerViewModel

object TimerScreenSpec : ScreenSpec {
    override val navHostRoute: String = "Timer"

    @Composable
    override fun TopBar(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        val viewModel: TimerViewModel = hiltViewModel(navBackStackEntry)
        TimerScreenAppBar(actions = viewModel)
    }

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        val viewModel: TimerViewModel = hiltViewModel(navBackStackEntry)
        val timeLeftInMillis by viewModel.timeLeftInMillis.observeAsState(0L)
        val currentTimeTargetInMillis by viewModel.currentTimeTargetInMillis.observeAsState(0L)
        val timerRunning by viewModel.timerRunning.observeAsState(false)
        val currentPhase by viewModel.currentPhase.observeAsState()

        TimerScreenContent(
            timeLeftInMillis = timeLeftInMillis,
            currentTimeTargetInMillis = currentTimeTargetInMillis,
            currentPhase = currentPhase,
            timerRunning = timerRunning,
            actions = viewModel,
        )
    }
}