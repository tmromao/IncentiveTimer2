package com.example.incentivetimer.features.timer

import android.os.CountDownTimer
import com.example.incentivetimer.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
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

    //TODO: 25/12/2021 Clean up this code and extract some logic into separate methods

    private fun startTimer() {

        resetPomodoroCounterIfTargetReached()
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

    private fun resetPomodoroCounterIfTargetReached() {
        val pomodorosCompleted = pomodorosCompletedFlow.value
        val pomodorosTarget = pomodorosTargetFlow.value
        val currentPhase = currentPhaseFlow.value
        if (pomodorosCompleted >= pomodorosTarget && currentPhase == PomodoroPhase.POMODORO) {
            pomodorosCompletedFlow.value = 0
        }
    }

    private fun startNextPhase() {
        val lastPhase = currentPhaseFlow.value
        val pomodorosCompleted = if(lastPhase == PomodoroPhase.POMODORO) pomodorosCompletedFlow.value + 1 else pomodorosCompletedFlow.value
        val pomodorosTarget = pomodorosTargetFlow.value
        pomodorosCompletedFlow.value = pomodorosCompleted

        val nextPhase = getNextPhase(
            lastPhase = lastPhase,
            pomodorosCompleted = pomodorosCompleted,
            pomodorosTarget = pomodorosTarget
        )

        currentPhaseFlow.value = nextPhase
        val nextTimeTarget = getTimeTargetForPhase(nextPhase)
        currentTimeTargetInMillisFlow.value = getTimeTargetForPhase(nextPhase)
        timeLeftInMillisFlow.value = nextTimeTarget
        startTimer()
    }

    private fun getNextPhase(
        lastPhase: PomodoroPhase,
        pomodorosCompleted: Int,
        pomodorosTarget: Int
    ): PomodoroPhase = when (lastPhase) {
        PomodoroPhase.POMODORO -> if (pomodorosCompleted >= pomodorosTarget) PomodoroPhase.LONG_BREAK else PomodoroPhase.SHORT_BREAK
        PomodoroPhase.SHORT_BREAK, PomodoroPhase.LONG_BREAK -> PomodoroPhase.POMODORO

    }

    private fun getTimeTargetForPhase(phase: PomodoroPhase): Long = when (phase) {
        PomodoroPhase.POMODORO -> POMODORO_DURATION_IN_MILLIS
        PomodoroPhase.SHORT_BREAK -> SHORT_BREAK_DURATION_IN_MILLIS
        PomodoroPhase.LONG_BREAK -> LONG_BREAK_DURATION_IN_MILLIS
    }

}