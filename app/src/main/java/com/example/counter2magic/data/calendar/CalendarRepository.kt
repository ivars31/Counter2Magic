package com.example.counter2magic.data.calendar

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.CalendarContract
import com.example.counter2magic.domain.model.CountdownEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val contentResolver: ContentResolver = context.contentResolver

    fun getUpcomingCalendarEvents(daysAhead: Int = 30): List<CalendarEvent> {
        val events = mutableListOf<CalendarEvent>()

        val now = System.currentTimeMillis()
        val endTime = now + (daysAhead.toLong() * 24 * 60 * 60 * 1000)

        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.ALL_DAY
        )

        val selection = "${CalendarContract.Events.DTSTART} >= ? AND ${CalendarContract.Events.DTSTART} <= ?"
        val selectionArgs = arrayOf(now.toString(), endTime.toString())
        val sortOrder = "${CalendarContract.Events.DTSTART} ASC"

        var cursor: Cursor? = null
        try {
            cursor = contentResolver.query(
                CalendarContract.Events.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )

            cursor?.let {
                while (it.moveToNext()) {
                    val id = it.getLong(it.getColumnIndexOrThrow(CalendarContract.Events._ID))
                    val title = it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.TITLE)) ?: "Untitled Event"
                    val startTime = it.getLong(it.getColumnIndexOrThrow(CalendarContract.Events.DTSTART))
                    val endTime = it.getLong(it.getColumnIndexOrThrow(CalendarContract.Events.DTEND))
                    val description = it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.DESCRIPTION)) ?: ""
                    val allDay = it.getInt(it.getColumnIndexOrThrow(CalendarContract.Events.ALL_DAY)) == 1

                    events.add(
                        CalendarEvent(
                            id = id,
                            title = title,
                            startTime = Instant.ofEpochMilli(startTime),
                            endTime = if (endTime > 0) Instant.ofEpochMilli(endTime) else null,
                            description = description,
                            isAllDay = allDay
                        )
                    )
                }
            }
        } catch (e: SecurityException) {
            // Calendar permission not granted
        } finally {
            cursor?.close()
        }

        return events
    }
}

data class CalendarEvent(
    val id: Long,
    val title: String,
    val startTime: Instant,
    val endTime: Instant?,
    val description: String,
    val isAllDay: Boolean
) {
    fun toCountdownEvent(): CountdownEvent = CountdownEvent(
        title = title,
        targetDateTime = startTime,
        description = description,
        isFromCalendar = true,
        calendarEventId = id
    )
}
