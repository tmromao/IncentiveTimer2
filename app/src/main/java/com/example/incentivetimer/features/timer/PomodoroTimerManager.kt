package com.example.incentivetimer.features.timer

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import com.example.incentivetimer.core.util.exhaustive
import com.example.incentivetimer.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import logcat.logcat
import javax.inject.Inject
import javax.inject.Singleton

enum class PomodoroPhase {
    POMODORO, SHORT_BREAK, LONG_BREAK
}

const val POMODORO_DURATION_IN_MILLIS = /*25 * 60 * 1_000L*/ 12000L
const val SHORT_BREAK_DURATION_IN_MILLIS = /*5 * 60 * 1_000L*/ 5000L
const val LONG_BREAK_DURATION_IN_MILLIS = /*15 * 60 * 1_000L*/ 8000L
const val POMODOROS_PER_SET = 4

private const val TICK_INTERVAL = 1000L

@Singleton
class PomodoroTimerManager @Inject constructor(
    @ApplicationScope private val applicationScope: CoroutineScope,
) {
    private val currentPhaseFlow = MutableStateFlow(PomodoroPhase.POMODORO)
    val currentPhase: Flow<PomodoroPhase> = currentPhaseFlow

    private val currentTimeTargetInMillisFlow = MutableStateFlow(POMODORO_DURATION_IN_MILLIS)
    val currentTimeTargetInMillis: Flow<Long> = currentTimeTargetInMillisFlow

    private val pomodorosCompletedFlow = MutableStateFlow(0)

    val pomodorosCompleted: Flow<Int> = pomodorosCompletedFlow
    private val pomodorosTargetFlow = MutableStateFlow(POMODOROS_PER_SET)

    val pomodorosTarget: Flow<Int> = pomodorosTargetFlow

    private var countDownTimer: CountDownTimer? = null

    private val timeLeftInMillisFlow = MutableStateFlow(POMODORO_DURATION_IN_MILLIS)
    val timeLeftInMillis: Flow<Long> = timeLeftInMillisFlow

    private val timerRunningFlow = MutableStateFlow(false)
    val timerRunning: Flow<Boolean> = timerRunningFlow

    fun startStopTimer() {
        val timerRunning = timerRunningFlow.value
        if (timerRunning) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        val timeLeftInMillis = timeLeftInMillisFlow.value
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillisFlow.value = millisUntilFinished
            }

            override fun onFinish() {
                stopTimer()
                startNextPhase()
            }
        }.start()
        timerRunningFlow.value = true
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
        timerRunningFlow.value = false

    }

    private fun startNextPhase() {
        val currentPhase = currentPhaseFlow.value
        val pomodorosCompleted = pomodorosCompletedFlow.value
        val pomodorosTarget = pomodorosTargetFlow.value

        val nextPhase = when (currentPhase) {
            PomodoroPhase.POMODORO -> if (pomodorosCompleted >= pomodorosTarget) PomodoroPhase.LONG_BREAK else PomodoroPhase.SHORT_BREAK
            PomodoroPhase.SHORT_BREAK, PomodoroPhase.LONG_BREAK -> PomodoroPhase.POMODORO
        }

        currentPhaseFlow.value = nextPhase
        val nextTimeTarget = when (nextPhase) {
            PomodoroPhase.POMODORO -> POMODORO_DURATION_IN_MILLIS
            PomodoroPhase.SHORT_BREAK -> SHORT_BREAK_DURATION_IN_MILLIS
            PomodoroPhase.LONG_BREAK -> LONG_BREAK_DURATION_IN_MILLIS
        }
        currentTimeTargetInMillisFlow.value = nextTimeTarget
        timeLeftInMillisFlow.value = nextTimeTarget
        startTimer()
    }


}