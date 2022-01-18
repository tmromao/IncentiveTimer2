package com.example.incentivetimer.features.timer

import android.os.CountDownTimer
import androidx.annotation.StringRes
import com.example.incentivetimer.R
import com.example.incentivetimer.core.notification.NotificationHelper
import com.example.incentivetimer.di.ApplicationScope
import com.zhuinden.flowcombinetuplekt.combineTuple
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

data class PomodoroTimerState(
    val timerRunning: Boolean,
    val currentPhase: PomodoroPhase,
    val timeLeftInMillis: Long,
    val timeTargetInMillis: Long,
    val pomodorosCompletedInSet: Int,
    val pomodorosPerSetTarget: Int,
    val pomodorosCompletedTotal: Int,
)

enum class PomodoroPhase(@StringRes val readableName: Int) {
    POMODORO(R.string.pomodoro), SHORT_BREAK(R.string.short_break), LONG_BREAK(R.string.long_break)
}

const val POMODORO_DURATION_IN_MILLIS = /*25 * 60 * 1_000L*/ 12000L
const val SHORT_BREAK_DURATION_IN_MILLIS = /*5 * 60 * 1_000L*/ 5000L
const val LONG_BREAK_DURATION_IN_MILLIS = /*15 * 60 * 1_000L*/ 8000L
const val POMODOROS_PER_SET = 4

private const val TICK_INTERVAL = 1000L

@Singleton
class PomodoroTimerManager @Inject constructor(
    private val timerServiceManager: TimerServiceManager,
    private val notificationHelper: NotificationHelper,
) {
    private val timerRunningFlow = MutableStateFlow(false)
    private val currentPhaseFlow = MutableStateFlow(PomodoroPhase.POMODORO)
    private val timeLeftInMillisFlow = MutableStateFlow(POMODORO_DURATION_IN_MILLIS)
    private val timeTargetInMillisFlow = MutableStateFlow(POMODORO_DURATION_IN_MILLIS)
    private val pomodorosCompletedInSetFlow = MutableStateFlow(0)
    private val pomodorosPerSetTargetFlow = MutableStateFlow(POMODOROS_PER_SET)
    private val pomodorosCompletedTotalFlow = MutableStateFlow(0)

    val pomodoroTimerState = combineTuple(
        timerRunningFlow,
        currentPhaseFlow,
        timeLeftInMillisFlow,
        timeTargetInMillisFlow,
        pomodorosCompletedInSetFlow,
        pomodorosPerSetTargetFlow,
        pomodorosCompletedTotalFlow,
    ).map { (timerRunning, currentPhase, timeLeftInMillis, timeTargetInMillis, pomodorosCompletedInSet, pomodorosPerSetTarget, pomodorosCompletedTotal) ->
        PomodoroTimerState(
            timerRunning = timerRunning,
            currentPhase = currentPhase,
            timeLeftInMillis = timeLeftInMillis,
            timeTargetInMillis = timeTargetInMillis,
            pomodorosCompletedInSet = pomodorosCompletedInSet,
            pomodorosPerSetTarget = pomodorosPerSetTarget,
            pomodorosCompletedTotal = pomodorosCompletedTotal,
        )
    }

    private var countDownTimer: CountDownTimer? = null

    fun startStopTimer() {
        notificationHelper.removeTimerCompletedNotification()
        val timerRunning = timerRunningFlow.value
        if (timerRunning) {
            stopTimer()
            timerServiceManager.stopTimerService()
        } else {
            startTimer()
            timerServiceManager.startTimerService()
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
                val currentPhase = currentPhaseFlow.value
                notificationHelper.showTimerCompleteNotification(currentPhase)
                stopTimer()
                if (currentPhase == PomodoroPhase.POMODORO) {
                    pomodorosCompletedTotalFlow.value++
                    pomodorosCompletedInSetFlow.value++
                }
                startNextPhase()
            }
        }.start()
        timerRunningFlow.value = true
        //timerServiceManager.startTimerService()
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
        timerRunningFlow.value = false
        timerServiceManager.stopTimerService()
    }

    private fun resetPomodoroCounterIfTargetReached() {
        val pomodorosCompleted = pomodorosCompletedInSetFlow.value
        val pomodorosTarget = pomodorosPerSetTargetFlow.value
        val currentPhase = currentPhaseFlow.value
        if (pomodorosCompleted >= pomodorosTarget && currentPhase == PomodoroPhase.POMODORO) {
            pomodorosCompletedInSetFlow.value = 0
        }
    }

    private fun startNextPhase() {
        val lastPhase = currentPhaseFlow.value
        val pomodorosCompletedInSet = pomodorosCompletedInSetFlow.value
        val pomodorosPerTarget = pomodorosPerSetTargetFlow.value
        pomodorosCompletedInSetFlow.value = pomodorosCompletedInSet

        setPomodoroPhase(getNextPhase(lastPhase, pomodorosCompletedInSet, pomodorosPerTarget))
        startTimer()
    }

    private fun setPomodoroPhase(newPhase: PomodoroPhase) {
        currentPhaseFlow.value = newPhase
        val newTimeTarget = getTimeTargetForPhase(newPhase)
        timeLeftInMillisFlow.value = newTimeTarget
        timeLeftInMillisFlow.value = newTimeTarget
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

    fun resetTimer() {
        stopTimer()
        timeLeftInMillisFlow.value = timeTargetInMillisFlow.value
    }

    fun resetPomodoroSet() {
        resetTimer()
        pomodorosCompletedInSetFlow.value = 0
        setPomodoroPhase(PomodoroPhase.POMODORO)
    }

    fun resetPomodoroCount() {
        pomodorosCompletedTotalFlow.value = 0
    }
}