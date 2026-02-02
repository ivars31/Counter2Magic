package com.example.counter2magic.domain.model

import java.time.Duration
import java.time.Instant
import java.util.UUID

data class Reminder(
    val id: String = UUID.randomUUID().toString(),
    val eventId: Long,
    val triggerTime: Instant,
    val offsetBeforeEvent: Duration,
    val isEnabled: Boolean = true
)

enum class ReminderOffset(val duration: Duration, val displayName: String) {
    FIFTEEN_MINUTES(Duration.ofMinutes(15), "15 minutes before"),
    THIRTY_MINUTES(Duration.ofMinutes(30), "30 minutes before"),
    ONE_HOUR(Duration.ofHours(1), "1 hour before"),
    ONE_DAY(Duration.ofDays(1), "1 day before")
}
