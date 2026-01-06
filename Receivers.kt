package com.aquareminder.app.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WaterReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationHelper = NotificationHelper(context)
        notificationHelper.showWaterReminderNotification()
    }
}

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule reminders after device reboot
            CoroutineScope(Dispatchers.IO).launch {
                val app = context.applicationContext as com.aquareminder.app.AquaReminderApp
                app.preferencesManager.getReminderSettings().collect { settings ->
                    val notificationHelper = NotificationHelper(context)
                    notificationHelper.scheduleReminders(settings)
                }
            }
        }
    }
}
