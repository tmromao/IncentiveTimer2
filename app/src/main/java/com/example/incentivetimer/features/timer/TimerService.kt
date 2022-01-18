package com.example.incentivetimer.features.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.incentivetimer.R
import com.example.incentivetimer.core.notification.NotificationHelper
import com.example.incentivetimer.core.notification.TIMER_SERVICE_NOTIFICATION_ID
import com.example.incentivetimer.di.ApplicationScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class TimerService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob())

    @Inject
    lateinit var pomodoroTimerManager: PomodoroTimerManager

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(
            TIMER_SERVICE_NOTIFICATION_ID,
            notificationHelper.getBaseTimerServiceNotification().build()
        )

        serviceScope.launch {
            pomodoroTimerManager.pomodoroTimerState.collect { timerState ->
                //delay(500)
                notificationHelper.updateTimerNotification(timerState)
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        notificationHelper.removeTimerServiceNotification()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

