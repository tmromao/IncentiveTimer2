package com.example.incentivetimer.features.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val pomodoroTimerManager: PomodoroTimerManager,
) : ViewModel(), TimerScreenActions {

    val timeLeftInMillis = pomodoroTimerManager.timeLeftInMillis.asLiveData()
    val timerRunning = pomodoroTimerManager.timerRunning.asLiveData()
    val currentTimeTargetInMillis = pomodoroTimerManager.currentTimeTargetInMillis.asLiveData()
    val currentPhase = pomodoroTimerManager.currentPhase.asLiveData()

    override fun onStartStopTimerClicked() {
        pomodoroTimerManager.startStopTimer()
    }

    override fun onStopTimerClicked() {
        TODO("Not yet implemented")
    }

    override fun onResetTimerClicked() {
        TODO("Not yet implemented")
    }

    override fun onResetPomodoroSetClicked() {
        TODO("Not yet implemented")
    }
}