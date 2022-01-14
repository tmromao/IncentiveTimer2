package com.example.incentivetimer.timer

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.incentivetimer.R
import com.example.incentivetimer.core.ui.composables.RoundedCornerCircularProgressIndicator
import com.example.incentivetimer.core.ui.composables.RoundedCornerCircularProgressIndicatorWithBackground
import com.example.incentivetimer.core.ui.theme.IncentiveTimerTheme

interface TimerScreenActions {

}

@Composable
fun TimerScreen(navController: NavController) {
    val timerRunning = true
    ScreenContent(timerRunning = timerRunning)
}

@Composable
fun TimerScreenAppBar() {
    TopAppBar(title = {
        Text(stringResource(R.string.timer))
    })
}

@Composable
private fun ScreenContent(
    timerRunning: Boolean,
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
        Text(text = "25:00", style = MaterialTheme.typography.h4)
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
            ScreenContent(timerRunning = true)
        }
    }
}