package com.example.counter2magic.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.counter2magic.domain.model.CountdownEvent
import java.time.Instant

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val targetDateTime: Long, // Epoch millis
    val description: String = "",
    val isFromCalendar: Boolean = false,
    val calendarEventId: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): CountdownEvent = CountdownEvent(
        id = id,
        title = title,
        targetDateTime = Instant.ofEpochMilli(targetDateTime),
        description = description,
        isFromCalendar = isFromCalendar,
        calendarEventId = calendarEventId,
        createdAt = Instant.ofEpochMilli(createdAt)
    )

    companion object {
        fun fromDomain(event: CountdownEvent): EventEntity = EventEntity(
            id = event.id,
            title = event.title,
            targetDateTime = event.targetDateTime.toEpochMilli(),
            description = event.description,
            isFromCalendar = event.isFromCalendar,
            calendarEventId = event.calendarEventId,
            createdAt = event.createdAt.toEpochMilli()
        )
    }
}
