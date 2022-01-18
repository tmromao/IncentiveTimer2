package com.example.incentivetimer.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.incentivetimer.R
import com.example.incentivetimer.application.ITActivity
import com.example.incentivetimer.features.timer.PomodoroPhase
import com.example.incentivetimer.features.timer.PomodoroTimerState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
) {
    private val notificationManager = NotificationManagerCompat.from(applicationContext)

    private val pendingIntentFlags /*mutabilityFlag*/ = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    //TODO: 26/12/2021 Let Intent navigate to timer screen
    private val openTimerIntent = Intent(
        Intent.ACTION_VIEW,
        "https://www.incentivetimer.com/timer".toUri(),
        applicationContext,
        ITActivity::class.java
    )

    private val openTimerPendingIntent = TaskStackBuilder.create(applicationContext).run {
        addNextIntentWithParentStack(openTimerIntent)
        getPendingIntent(0, pendingIntentFlags)
    }

    init {
        createNotificationChannel()
    }

    fun getBaseTimerServiceNotification() =
        NotificationCompat.Builder(applicationContext, TIMER_SERVICE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_timer)
            .setContentIntent(openTimerPendingIntent)
            .setSilent(true)
            .setOnlyAlertOnce(true)

    fun updateTimerNotification(timerState: PomodoroTimerState) {
        val notificationUpdate = getBaseTimerServiceNotification()
            .setContentTitle(applicationContext.getString(timerState.currentPhase.readableName))
            .setContentText(timerState.timeLeftInMillis.toString())
            .build()
        notificationManager.notify(TIMER_SERVICE_NOTIFICATION_ID, notificationUpdate)
    }

    fun showTimerCompleteNotification(finishedPhase: PomodoroPhase) {
        val title: Int
        val text: Int

        when (finishedPhase) {
            PomodoroPhase.POMODORO -> {
                title = R.string.pomodoro_completed
                text = R.string.time_for_a_break
            }
            PomodoroPhase.SHORT_BREAK, PomodoroPhase.LONG_BREAK -> {
                title = R.string.break_is_over
                text = R.string.time_to_get_back_to_work
            }
        }
        val timerCompletedNotification =
            NotificationCompat.Builder(applicationContext, TIMER_SERVICE_CHANNEL_ID)
                .setContentTitle(applicationContext.getString(title))
                .setContentText(applicationContext.getString(text))
                .setSmallIcon(R.drawable.ic_timer)
                .setContentIntent(openTimerPendingIntent)
                .build()
        notificationManager.notify(TIMER_COMPLETED_NOTIFICATION_ID, timerCompletedNotification)
    }

    fun removeTimerServiceNotification() {
        notificationManager.cancel(TIMER_SERVICE_NOTIFICATION_ID)
    }

    fun removeTimerCompletedNotification() {
        notificationManager.cancel(TIMER_COMPLETED_NOTIFICATION_ID)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val timerServiceChannel = NotificationChannel(
                TIMER_SERVICE_CHANNEL_ID,
                applicationContext.getString(R.string.timer_service_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            timerServiceChannel.apply {
                setSound(null, null)
            }
            notificationManager.createNotificationChannel(timerServiceChannel)
        }
    }
}

private const val TIMER_SERVICE_CHANNEL_ID = "timer_service_channel"
const val TIMER_SERVICE_NOTIFICATION_ID = 123
private const val TIMER_COMPLETED_NOTIFICATION_ID = 124