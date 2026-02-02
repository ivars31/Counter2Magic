package com.example.counter2magic.data.repository

import com.example.counter2magic.data.local.db.dao.ReminderDao
import com.example.counter2magic.data.local.db.entity.ReminderEntity
import com.example.counter2magic.domain.model.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepository @Inject constructor(
    private val reminderDao: ReminderDao
) {

    fun getRemindersForEvent(eventId: Long): Flow<List<Reminder>> =
        reminderDao.getRemindersForEvent(eventId).map { entities ->
            entities.map { it.toDomain() }
        }

    fun getEnabledUpcomingReminders(): Flow<List<Reminder>> =
        reminderDao.getEnabledUpcomingReminders().map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun getReminderById(id: String): Reminder? =
        reminderDao.getReminderById(id)?.toDomain()

    suspend fun insertReminder(reminder: Reminder) =
        reminderDao.insertReminder(ReminderEntity.fromDomain(reminder))

    suspend fun updateReminder(reminder: Reminder) =
        reminderDao.updateReminder(ReminderEntity.fromDomain(reminder))

    suspend fun deleteReminder(reminder: Reminder) =
        reminderDao.deleteReminder(ReminderEntity.fromDomain(reminder))

    suspend fun deleteReminderById(id: String) =
        reminderDao.deleteReminderById(id)

    suspend fun deleteRemindersForEvent(eventId: Long) =
        reminderDao.deleteRemindersForEvent(eventId)
}
