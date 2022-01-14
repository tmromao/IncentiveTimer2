package com.example.incentivetimer.timer

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.incentivetimer.R
import com.example.incentivetimer.core.ui.composables.RoundedCornerCircularProgressIndicator
import com.example.incentivetimer.core.ui.composables.RoundedCornerCircularProgressIndicatorWithBackground
import com.example.incentivetimer.core.ui.theme.IncentiveTimerTheme
import com.example.incentivetimer.core.ui.theme.PrimaryLightAlpha

interface TimerScreenActions {
    fun onResetTimerClicked()
    fun onResetPomodoroSetClicked()
}

/*@Composable
fun TimerScreen(
    navController: NavController,
) {
    val viewModel: TimerViewModel = hiltViewModel()
    val timerRunning = true
    ScreenContent(
        timerRunning = timerRunning,
        actions = viewModel,
    )
}*/

@Composable
fun TimerScreenAppBar(
    actions: TimerScreenActions,
) {
    TopAppBar(
        title = {
            Text(stringResource(R.string.timer))
        },
        actions = {
            Box {
                var expanded by remember { mutableStateOf(false) }
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.open_menu)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(onClick = {
                        expanded = false
                        actions.onResetTimerClicked()
                    }) {
                        Text(stringResource(R.string.reset_timer))
                    }
                    DropdownMenuItem(onClick = {
                        expanded = false
                        actions.onResetPomodoroSetClicked()
                    }) {
                        Text(stringResource(R.string.reset_pomodoro_set))
                    }
                }

            }
        }
    )
}

@Composable
fun TimerScreenContent(
    timerRunning: Boolean,
    actions: TimerScreenActions,
) {
    Scaffold() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Timer()
            Spacer(Modifier.height(48.dp))
            TimerStartStopButton(timerRunning = timerRunning)
        }
    }
}

@Composable
private fun Timer(
    modifier: Modifier = Modifier,
) {
    Box(modifier.size(250.dp), contentAlignment = Alignment.Center) {
        RoundedCornerCircularProgressIndicatorWithBackground(
            progress = .6f,
            modifier = modifier
                .fillMaxSize()
                .scale(scaleX = -1f, scaleY = 1f),
            strokeWidth = 16.dp
        )
        Column {
            Box(Modifier.background(Color.Green)) {
                Text(
                    text = "25:00",
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.align(Alignment.Center)
                )
                PomodorosCompletedIndicatorRow(
                    pomodorosCompleted = 3, modifier = Modifier.align(
                        Alignment.BottomStart
                    )
                )
            }
        }
    }
}

@Composable
private fun TimerStartStopButton(
    timerRunning: Boolean,
    modifier: Modifier = Modifier,
) {
    val contentDescription =
        stringResource(if (!timerRunning) R.string.start_timer else R.string.stop_timer)

    FloatingActionButton(onClick = { /*TODO*/ }, modifier = modifier.size(64.dp)) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = contentDescription,
/*
            tint = MaterialTheme.colors.primary,
*/
            modifier = modifier
                .fillMaxSize()
                .padding(4.dp),
        )
    }
}

@Composable
private fun PomodorosCompletedIndicatorRow(
    pomodorosCompleted: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        SinglePomodoroCompletedIndicator(completed = pomodorosCompleted > 0)
        Spacer(modifier = Modifier.width(4.dp))
        SinglePomodoroCompletedIndicator(completed = pomodorosCompleted > 1)
        Spacer(modifier = Modifier.width(4.dp))
        SinglePomodoroCompletedIndicator(completed = pomodorosCompleted > 2)
        Spacer(modifier = Modifier.width(4.dp))
        SinglePomodoroCompletedIndicator(completed = pomodorosCompleted > 3)

    }
}

@Composable
private fun SinglePomodoroCompletedIndicator(
    completed: Boolean,
    modifier: Modifier = Modifier,
) {
    val color =
        if (!completed) MaterialTheme.colors.primary.copy(alpha = PrimaryLightAlpha) else MaterialTheme.colors.primary
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(8.dp)
            .background(color)
    )

}

@Preview(
    name = "Light mode",
    uiMode = UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark mode",
    uiMode = UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun ScreenContentPreview() {
    IncentiveTimerTheme() {
        Surface() {
            TimerScreenContent(timerRunning = true,
                actions = object : TimerScreenActions {
                    override fun onResetTimerClicked() {}
                    override fun onResetPomodoroSetClicked() {}
                }
            )
        }
    }
}