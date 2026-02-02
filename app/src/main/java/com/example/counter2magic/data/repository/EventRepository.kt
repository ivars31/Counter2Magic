package com.example.counter2magic.data.repository

import com.example.counter2magic.data.local.db.dao.EventDao
import com.example.counter2magic.data.local.db.entity.EventEntity
import com.example.counter2magic.domain.model.CountdownEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val eventDao: EventDao
) {

    fun getAllEvents(): Flow<List<CountdownEvent>> =
        eventDao.getAllEvents().map { entities ->
            entities.map { it.toDomain() }
        }

    fun getUpcomingEvents(): Flow<List<CountdownEvent>> =
        eventDao.getUpcomingEvents().map { entities ->
            entities.map { it.toDomain() }
        }

    fun getUpcomingEventsLimit(limit: Int): Flow<List<CountdownEvent>> =
        eventDao.getUpcomingEventsLimit(limit = limit).map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun getEventById(id: Long): CountdownEvent? =
        eventDao.getEventById(id)?.toDomain()

    suspend fun getEventByCalendarId(calendarEventId: Long): CountdownEvent? =
        eventDao.getEventByCalendarId(calendarEventId)?.toDomain()

    suspend fun insertEvent(event: CountdownEvent): Long =
        eventDao.insertEvent(EventEntity.fromDomain(event))

    suspend fun updateEvent(event: CountdownEvent) =
        eventDao.updateEvent(EventEntity.fromDomain(event))

    suspend fun deleteEvent(event: CountdownEvent) =
        eventDao.deleteEvent(EventEntity.fromDomain(event))

    suspend fun deleteEventById(id: Long) =
        eventDao.deleteEventById(id)

    suspend fun deleteAllCalendarEvents() =
        eventDao.deleteAllCalendarEvents()
}
