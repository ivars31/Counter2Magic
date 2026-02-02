package com.example.counter2magic.domain.model

import java.time.Instant

data class CountdownEvent(
    val id: Long = 0,
    val title: String,
    val targetDateTime: Instant,
    val description: String = "",
    val isFromCalendar: Boolean = false,
    val calendarEventId: Long? = null,
    val createdAt: Instant = Instant.now()
)
