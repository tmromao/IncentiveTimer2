package com.example.incentivetimer.features.timer

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
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
import androidx.compose.ui.modifier.modifierLocalConsumer
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
                    DropdownMenuItem(onClick = {
                        expanded = false
                        actions.onResetPomodoroCountClicked()
                    }) {
                        Text(stringResource(R.string.reset_pomodoro_count))
                    }
                }
            }
        }
    )
}

@Composable
fun TimerScreenContent(
    pomodoroTimerState: PomodoroTimerState?,
    actions: TimerScreenActions,
) {
    val timerRunning = pomodoroTimerState?.timerRunning ?: false

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Timer(pomodoroTimerState = pomodoroTimerState)
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
    pomodoroTimerState: PomodoroTimerState?,
    modifier: Modifier = Modifier,
) {
    val timeLeftInMillis = pomodoroTimerState?.timeLeftInMillis ?: 0L
    val timeTargetInMillis = pomodoroTimerState?.timeTargetInMillis ?: 0L
    val currentPhase = pomodoroTimerState?.currentPhase
    val pomodorosCompletedInSet = pomodoroTimerState?.pomodorosCompletedInSet ?: 0
    val pomodorosPerSetTarget = pomodoroTimerState?.pomodorosPerSetTarget ?: 0
    val pomodorosCompletedTotal = pomodoroTimerState?.pomodorosCompletedTotal ?: 0

    val progress = timeLeftInMillis.toFloat() / timeTargetInMillis.toFloat()

    Box(modifier.size(250.dp), contentAlignment = Alignment.Center) {
        RoundedCornerCircularProgressIndicatorWithBackground(
            progress = progress,
            modifier = modifier
                .fillMaxSize()
                .scale(scaleX = -1f, scaleY = 1f),
            strokeWidth = 16.dp
        )
        Text(
            text = timeLeftInMillis.toString(),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.align(Alignment.Center)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            val phaseText = if (currentPhase !=null) stringResource(currentPhase.readableName) else ""
            Text(phaseText, Modifier.padding(top = 48.dp), style = MaterialTheme.typography.body2)
            Spacer(modifier = Modifier.height(4.dp))
            val timerRunning = pomodoroTimerState?.timerRunning ?: false
            PomodorosCompletedIndicatorRow(
                pomodorosCompletedInSet = pomodorosCompletedInSet,
                pomodorosPerSetTarget = pomodorosPerSetTarget,
                timerRunning = timerRunning,
                currentPhase = currentPhase,
            )
        }
        //Total number of pomodoros
        Text(
            text = stringResource(R.string.total) + ": $pomodorosCompletedTotal",
            style = MaterialTheme.typography.body2,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        )

    }
}

@Composable
private fun TimerStartStopButton(
    timerRunning: Boolean,
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

            modifier = modifier
                .fillMaxSize()
                .padding(4.dp),
        )
    }
}

@Composable
private fun PomodorosCompletedIndicatorRow(
    pomodorosCompletedInSet: Int,
    pomodorosPerSetTarget: Int,
    timerRunning: Boolean,
    currentPhase: PomodoroPhase?,
    modifier: Modifier = Modifier
) {
    val pomodoroInProgress = timerRunning && currentPhase == PomodoroPhase.POMODORO

    Row(modifier) {
        repeat(pomodorosPerSetTarget) { index ->
            key(index) {
                SinglePomodoroCompletedIndicator(
                    completed = pomodorosCompletedInSet > index,
                    inProgress = pomodoroInProgress && pomodorosCompletedInSet == index
                )
                if (index < pomodorosPerSetTarget - 1) {
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }


}

@Composable
private fun SinglePomodoroCompletedIndicator(
    completed: Boolean,
    inProgress: Boolean,
    modifier: Modifier = Modifier,
) {
    val uncompletedColor = MaterialTheme.colors.primary.copy(alpha = PrimaryLightAlpha)
    val completedColor = MaterialTheme.colors.primary

    val infiniteTransition = rememberInfiniteTransition()
    val inProgressColor by infiniteTransition.animateColor(
        initialValue = uncompletedColor,
        targetValue = completedColor,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        )
    )
    val color = when {
        completed -> completedColor
        inProgress -> inProgressColor
        else -> uncompletedColor
    }

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
                pomodoroTimerState = PomodoroTimerState(
                    timerRunning = true,
                    timeLeftInMillis = 15 * 60 * 1000L,
                    timeTargetInMillis = POMODORO_DURATION_IN_MILLIS,
                    currentPhase = PomodoroPhase.POMODORO,
                    pomodorosCompletedInSet = 3,
                    pomodorosCompletedTotal = 5,
                    pomodorosPerSetTarget = 4,
                ),
                actions = object : TimerScreenActions {
                    override fun onResetTimerClicked() {}
                    override fun onResetPomodoroSetClicked() {}
                    override fun onStartStopTimerClicked() {}
                    override fun onStopTimerClicked() {
                        TODO("Not yet implemented")
                    }

                    override fun onResetPomodoroCountClicked() {}
                },
            )
        }
    }
}