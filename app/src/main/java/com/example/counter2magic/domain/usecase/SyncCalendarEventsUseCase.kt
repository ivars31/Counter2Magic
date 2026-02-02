package com.example.counter2magic.domain.usecase

import com.example.counter2magic.data.calendar.CalendarRepository
import com.example.counter2magic.data.repository.EventRepository
import javax.inject.Inject

class SyncCalendarEventsUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository,
    private val eventRepository: EventRepository
) {

    suspend operator fun invoke(): SyncResult {
        val calendarEvents = calendarRepository.getUpcomingCalendarEvents()
        var added = 0
        var updated = 0

        for (calendarEvent in calendarEvents) {
            val existingEvent = eventRepository.getEventByCalendarId(calendarEvent.id)

            if (existingEvent == null) {
                eventRepository.insertEvent(calendarEvent.toCountdownEvent())
                added++
            } else {
                // Update if the calendar event has changed
                if (existingEvent.title != calendarEvent.title ||
                    existingEvent.targetDateTime != calendarEvent.startTime ||
                    existingEvent.description != calendarEvent.description
                ) {
                    eventRepository.updateEvent(
                        existingEvent.copy(
                            title = calendarEvent.title,
                            targetDateTime = calendarEvent.startTime,
                            description = calendarEvent.description
                        )
                    )
                    updated++
                }
            }
        }

        return SyncResult(
            added = added,
            updated = updated,
            total = calendarEvents.size
        )
    }
}

data class SyncResult(
    val added: Int,
    val updated: Int,
    val total: Int
)
