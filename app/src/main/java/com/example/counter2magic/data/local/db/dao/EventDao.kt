package com.example.counter2magic.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.counter2magic.data.local.db.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM events ORDER BY targetDateTime ASC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE targetDateTime > :currentTime ORDER BY targetDateTime ASC")
    fun getUpcomingEvents(currentTime: Long = System.currentTimeMillis()): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE targetDateTime > :currentTime ORDER BY targetDateTime ASC LIMIT :limit")
    fun getUpcomingEventsLimit(currentTime: Long = System.currentTimeMillis(), limit: Int): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: Long): EventEntity?

    @Query("SELECT * FROM events WHERE calendarEventId = :calendarEventId")
    suspend fun getEventByCalendarId(calendarEventId: Long): EventEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity): Long

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Delete
    suspend fun deleteEvent(event: EventEntity)

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEventById(id: Long)

    @Query("DELETE FROM events WHERE isFromCalendar = 1")
    suspend fun deleteAllCalendarEvents()
}
