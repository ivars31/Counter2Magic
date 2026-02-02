package com.example.counter2magic.notification

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.counter2magic.domain.model.Reminder
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.Instant
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val workManager = WorkManager.getInstance(context)

    fun scheduleReminder(reminder: Reminder, eventTitle: String) {
        val delay = Duration.between(Instant.now(), reminder.triggerTime)

        if (delay.isNegative) {
            return // Don't schedule reminders in the past
        }

        val inputData = Data.Builder()
            .putString(ReminderWorker.KEY_REMINDER_ID, reminder.id)
            .putLong(ReminderWorker.KEY_EVENT_ID, reminder.eventId)
            .putString(ReminderWorker.KEY_EVENT_TITLE, eventTitle)
            .putLong(ReminderWorker.KEY_OFFSET_MINUTES, reminder.offsetBeforeEvent.toMinutes())
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay.toMillis(), TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(REMINDER_WORK_TAG)
            .addTag(reminder.id)
            .build()

        workManager.enqueueUniqueWork(
            reminder.id,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancelReminder(reminderId: String) {
        workManager.cancelUniqueWork(reminderId)
    }

    fun cancelAllReminders() {
        workManager.cancelAllWorkByTag(REMINDER_WORK_TAG)
    }

    companion object {
        const val REMINDER_WORK_TAG = "reminder_work"
    }
}
