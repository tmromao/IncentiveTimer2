package com.example.incentivetimer.features.timer

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.incentivetimer.R
import com.example.incentivetimer.core.ui.composables.RoundedCornerCircularProgressIndicatorWithBackground
import com.example.incentivetimer.core.ui.theme.IncentiveTimerTheme
import com.example.incentivetimer.core.ui.theme.PrimaryLightAlpha

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
    timeLeftInMillis: Long,
    currentTimeTargetInMillis: Long,
    currentPhase: PomodoroPhase?,
    timerRunning: Boolean,
    actions: TimerScreenActions,
) {
    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Timer(
                timeLeftInMillis,
                currentTimeTargetInMillis = currentTimeTargetInMillis,
                currentPhase = currentPhase,
            )
            Spacer(Modifier.height(48.dp))
            TimerStartStopButton(
                timerRunning = timerRunning,
                actions = actions
            )
        }
    }
}

@Composable
private fun Timer(
    timeLeftInMillis: Long,
    currentTimeTargetInMillis: Long,
    currentPhase: PomodoroPhase?,
    modifier: Modifier = Modifier,
) {

    val progress = timeLeftInMillis.toFloat() / currentTimeTargetInMillis.toFloat()

    Box(modifier.size(250.dp), contentAlignment = Alignment.Center) {
        RoundedCornerCircularProgressIndicatorWithBackground(
            progress = progress,
            modifier = modifier
                .fillMaxSize()
                .scale(scaleX = -1f, scaleY = 1f),
            strokeWidth = 16.dp
        )
        Column {
            Box(Modifier.background(Color.Green)) {
                Text(
                    text = timeLeftInMillis.toString(),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.align(Alignment.Center)
                )
                PomodorosCompletedIndicatorRow(
                    pomodorosCompleted = 3, modifier = Modifier
                        .align(
                            Alignment.BottomStart
                        )
                        .padding(top = 60.dp)
                )
            }
            Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()) {
                val phaseText = when (currentPhase) {
                    PomodoroPhase.POMODORO -> stringResource(R.string.pomodoro).uppercase()
                    PomodoroPhase.SHORT_BREAK -> stringResource(R.string.short_break).uppercase()
                    PomodoroPhase.LONG_BREAK -> stringResource(R.string.long_break).uppercase()
                    null -> ""
                }
                Text(phaseText, Modifier.padding(top = 48.dp))
            }
        }
    }
}

@Composable
private fun TimerStartStopButton(
    timerRunning: Boolean,
    //currentTimeTargetInMillis: Long,
    actions: TimerScreenActions,
    modifier: Modifier = Modifier,
) {
    val startStopIcon = if (!timerRunning) Icons.Default.PlayArrow else Icons.Default.Pause
    val contentDescription =
        stringResource(if (!timerRunning) R.string.start_timer else R.string.stop_timer)

    FloatingActionButton(
        onClick = { actions.onStartStopTimerClicked() },
        modifier = modifier.size(64.dp)
    ) {
        Icon(
            imageVector = startStopIcon,
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
            TimerScreenContent(
                timerRunning = true,
                actions = object : TimerScreenActions {
                    override fun onStartStopTimerClicked() {}
                    override fun onStopTimerClicked() {}

                    override fun onResetTimerClicked() {}
                    override fun onResetPomodoroSetClicked() {}
                },
                timeLeftInMillis = 15 * 60 * 1000L,
                currentTimeTargetInMillis = POMODORO_DURATION_IN_MILLIS,
                currentPhase = PomodoroPhase.POMODORO,
            )
        }
    }
}