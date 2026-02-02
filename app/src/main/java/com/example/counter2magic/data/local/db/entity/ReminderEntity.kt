package com.example.counter2magic.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.counter2magic.domain.model.Reminder
import java.time.Duration
import java.time.Instant

@Entity(
    tableName = "reminders",
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("eventId")]
)
data class ReminderEntity(
    @PrimaryKey
    val id: String,
    val eventId: Long,
    val triggerTime: Long, // Epoch millis
    val offsetSeconds: Long, // Duration in seconds
    val isEnabled: Boolean = true
) {
    fun toDomain(): Reminder = Reminder(
        id = id,
        eventId = eventId,
        triggerTime = Instant.ofEpochMilli(triggerTime),
        offsetBeforeEvent = Duration.ofSeconds(offsetSeconds),
        isEnabled = isEnabled
    )

    companion object {
        fun fromDomain(reminder: Reminder): ReminderEntity = ReminderEntity(
            id = reminder.id,
            eventId = reminder.eventId,
            triggerTime = reminder.triggerTime.toEpochMilli(),
            offsetSeconds = reminder.offsetBeforeEvent.seconds,
            isEnabled = reminder.isEnabled
        )
    }
}
