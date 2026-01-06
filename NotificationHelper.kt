package com.aquareminder.app.notification

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.aquareminder.app.R
import com.aquareminder.app.model.ReminderSettings
import com.aquareminder.app.ui.MainActivity
import java.util.*

class NotificationHelper(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    companion object {
        const val CHANNEL_ID = "water_reminder_channel"
        const val NOTIFICATION_ID = 1001
        private const val REQUEST_CODE_BASE = 2000
    }

    fun showWaterReminderNotification() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_water_drop)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun scheduleReminders(settings: ReminderSettings) {
        if (!settings.enabled) {
            cancelAllReminders()
            return
        }

        // Cancel existing reminders
        cancelAllReminders()

        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        // Calculate first reminder time
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        var reminderTime = calendar.clone() as Calendar
        reminderTime.set(Calendar.HOUR_OF_DAY, settings.startHour)
        reminderTime.set(Calendar.MINUTE, settings.startMinute)

        // If start time has passed today, start from next interval
        if (reminderTime.timeInMillis < System.currentTimeMillis()) {
            val minutesUntilNext = settings.intervalMinutes
            reminderTime.add(Calendar.MINUTE, minutesUntilNext)
        }

        // Schedule all reminders for today
        var requestCode = REQUEST_CODE_BASE
        while (isWithinReminderPeriod(reminderTime, settings)) {
            scheduleReminder(reminderTime.timeInMillis, requestCode++)
            reminderTime.add(Calendar.MINUTE, settings.intervalMinutes)
        }
    }

    private fun isWithinReminderPeriod(time: Calendar, settings: ReminderSettings): Boolean {
        val hour = time.get(Calendar.HOUR_OF_DAY)
        val minute = time.get(Calendar.MINUTE)
        val timeInMinutes = hour * 60 + minute

        return timeInMinutes <= settings.getEndTimeInMinutes()
    }

    private fun scheduleReminder(triggerTime: Long, requestCode: Int) {
        val intent = Intent(context, WaterReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }

    fun cancelAllReminders() {
        for (i in 0 until 50) { // Cancel up to 50 possible reminders
            val intent = Intent(context, WaterReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                REQUEST_CODE_BASE + i,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }
    }
}
