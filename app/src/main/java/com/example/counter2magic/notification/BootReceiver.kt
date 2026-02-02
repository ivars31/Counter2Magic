package com.example.counter2magic.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.counter2magic.data.local.db.CountdownDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            rescheduleReminders(context)
        }
    }

    private fun rescheduleReminders(context: Context) {
        scope.launch {
            val database = CountdownDatabase.getInstance(context)
            val reminderScheduler = ReminderScheduler(context)

            // Get all enabled upcoming reminders
            val reminders = database.reminderDao()
                .getEnabledUpcomingReminders()
                .first()

            // Reschedule each reminder
            reminders.forEach { reminderEntity ->
                val reminder = reminderEntity.toDomain()
                val event = database.eventDao().getEventById(reminder.eventId)
                if (event != null) {
                    reminderScheduler.scheduleReminder(reminder, event.title)
                }
            }
        }
    }
}
