package com.example.incentivetimer.timer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(

) :ViewModel(), TimerScreenActions {
    override fun onResetTimerClicked() {
        TODO("Not yet implemented")
    }

    override fun onResetPomodoroSetClicked() {
        TODO("Not yet implemented")
    }
}