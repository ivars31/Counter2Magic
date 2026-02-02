package com.example.counter2magic.domain.formatter

import com.example.counter2magic.domain.model.TimeRemaining
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AdaptiveTimeFormatterTest {

    private lateinit var formatter: AdaptiveTimeFormatter

    @Before
    fun setUp() {
        formatter = AdaptiveTimeFormatter()
    }

    @Test
    fun `format seconds when less than 60 seconds`() {
        val timeRemaining = TimeRemaining.fromSeconds(45)
        val result = formatter.format(timeRemaining)

        assertEquals("45", result.primaryValue)
        assertEquals("seconds", result.primaryUnit)
        assertEquals(null, result.secondaryValue)
    }

    @Test
    fun `format singular second`() {
        val timeRemaining = TimeRemaining.fromSeconds(1)
        val result = formatter.format(timeRemaining)

        assertEquals("1", result.primaryValue)
        assertEquals("second", result.primaryUnit)
    }

    @Test
    fun `format minutes and seconds when less than 1 hour`() {
        val timeRemaining = TimeRemaining.fromSeconds(125) // 2 min 5 sec
        val result = formatter.format(timeRemaining)

        assertEquals("2", result.primaryValue)
        assertEquals("minutes", result.primaryUnit)
        assertEquals("5", result.secondaryValue)
        assertEquals("seconds", result.secondaryUnit)
    }

    @Test
    fun `format hours and minutes when less than 1 day`() {
        val timeRemaining = TimeRemaining.fromSeconds(7500) // 2 hours 5 min
        val result = formatter.format(timeRemaining)

        assertEquals("2", result.primaryValue)
        assertEquals("hours", result.primaryUnit)
        assertEquals("5", result.secondaryValue)
        assertEquals("minutes", result.secondaryUnit)
    }

    @Test
    fun `format days and hours when less than 7 days`() {
        val timeRemaining = TimeRemaining.fromSeconds(3 * 86400 + 5 * 3600) // 3 days 5 hours
        val result = formatter.format(timeRemaining)

        assertEquals("3", result.primaryValue)
        assertEquals("days", result.primaryUnit)
        assertEquals("5", result.secondaryValue)
        assertEquals("hours", result.secondaryUnit)
    }

    @Test
    fun `format weeks and days when less than 30 days`() {
        val timeRemaining = TimeRemaining.fromSeconds(15 * 86400) // 15 days = 2 weeks 1 day
        val result = formatter.format(timeRemaining)

        assertEquals("2", result.primaryValue)
        assertEquals("weeks", result.primaryUnit)
        assertEquals("1", result.secondaryValue)
        assertEquals("day", result.secondaryUnit)
    }

    @Test
    fun `format months and days when 30 days or more`() {
        val timeRemaining = TimeRemaining.fromSeconds(45 * 86400) // 45 days = 1 month 15 days
        val result = formatter.format(timeRemaining)

        assertEquals("1", result.primaryValue)
        assertEquals("month", result.primaryUnit)
        assertEquals("15", result.secondaryValue)
        assertEquals("days", result.secondaryUnit)
    }

    @Test
    fun `format past time with negative prefix`() {
        val timeRemaining = TimeRemaining.fromSeconds(-3600) // 1 hour ago
        val result = formatter.format(timeRemaining)

        assertEquals("-1", result.primaryValue)
        assertEquals("hour", result.primaryUnit)
    }

    @Test
    fun `formatCompact returns correct format for seconds`() {
        val timeRemaining = TimeRemaining.fromSeconds(45)
        val result = formatter.formatCompact(timeRemaining)

        assertEquals("45s", result)
    }

    @Test
    fun `formatCompact returns correct format for minutes`() {
        val timeRemaining = TimeRemaining.fromSeconds(125) // 2 min 5 sec
        val result = formatter.formatCompact(timeRemaining)

        assertEquals("2m 5s", result)
    }

    @Test
    fun `formatCompact returns correct format for hours`() {
        val timeRemaining = TimeRemaining.fromSeconds(7500) // 2 hours 5 min
        val result = formatter.formatCompact(timeRemaining)

        assertEquals("2h 5m", result)
    }

    @Test
    fun `formatCompact returns correct format for days`() {
        val timeRemaining = TimeRemaining.fromSeconds(3 * 86400 + 5 * 3600) // 3 days 5 hours
        val result = formatter.formatCompact(timeRemaining)

        assertEquals("3d 5h", result)
    }

    @Test
    fun `formatCompact handles past times`() {
        val timeRemaining = TimeRemaining.fromSeconds(-3600) // 1 hour ago
        val result = formatter.formatCompact(timeRemaining)

        assertEquals("-1h 0m", result)
    }
}
