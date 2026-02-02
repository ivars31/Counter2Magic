package com.example.counter2magic.domain.formatter

import com.example.counter2magic.domain.model.TimeRemaining
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdaptiveTimeFormatter @Inject constructor() {

    fun format(timeRemaining: TimeRemaining): FormattedTime {
        val prefix = if (timeRemaining.isPast) "-" else ""

        return when {
            timeRemaining.absoluteSeconds < 60 -> {
                FormattedTime(
                    primaryValue = "$prefix${timeRemaining.seconds}",
                    primaryUnit = if (timeRemaining.seconds == 1L) "second" else "seconds",
                    secondaryValue = null,
                    secondaryUnit = null
                )
            }
            timeRemaining.absoluteSeconds < 3600 -> {
                FormattedTime(
                    primaryValue = "$prefix${timeRemaining.minutes}",
                    primaryUnit = if (timeRemaining.minutes == 1L) "minute" else "minutes",
                    secondaryValue = timeRemaining.seconds.toString(),
                    secondaryUnit = if (timeRemaining.seconds == 1L) "second" else "seconds"
                )
            }
            timeRemaining.absoluteSeconds < 86400 -> {
                FormattedTime(
                    primaryValue = "$prefix${timeRemaining.hours}",
                    primaryUnit = if (timeRemaining.hours == 1L) "hour" else "hours",
                    secondaryValue = timeRemaining.minutes.toString(),
                    secondaryUnit = if (timeRemaining.minutes == 1L) "minute" else "minutes"
                )
            }
            timeRemaining.days < 7 -> {
                FormattedTime(
                    primaryValue = "$prefix${timeRemaining.days}",
                    primaryUnit = if (timeRemaining.days == 1L) "day" else "days",
                    secondaryValue = timeRemaining.hours.toString(),
                    secondaryUnit = if (timeRemaining.hours == 1L) "hour" else "hours"
                )
            }
            timeRemaining.days < 30 -> {
                val weeks = timeRemaining.weeks
                val remainingDays = timeRemaining.days % 7
                FormattedTime(
                    primaryValue = "$prefix$weeks",
                    primaryUnit = if (weeks == 1L) "week" else "weeks",
                    secondaryValue = remainingDays.toString(),
                    secondaryUnit = if (remainingDays == 1L) "day" else "days"
                )
            }
            else -> {
                val months = timeRemaining.months
                val remainingDays = timeRemaining.days % 30
                FormattedTime(
                    primaryValue = "$prefix$months",
                    primaryUnit = if (months == 1L) "month" else "months",
                    secondaryValue = remainingDays.toString(),
                    secondaryUnit = if (remainingDays == 1L) "day" else "days"
                )
            }
        }
    }

    fun formatCompact(timeRemaining: TimeRemaining): String {
        val prefix = if (timeRemaining.isPast) "-" else ""

        return when {
            timeRemaining.absoluteSeconds < 60 -> "${prefix}${timeRemaining.seconds}s"
            timeRemaining.absoluteSeconds < 3600 -> "${prefix}${timeRemaining.minutes}m ${timeRemaining.seconds}s"
            timeRemaining.absoluteSeconds < 86400 -> "${prefix}${timeRemaining.hours}h ${timeRemaining.minutes}m"
            timeRemaining.days < 7 -> "${prefix}${timeRemaining.days}d ${timeRemaining.hours}h"
            timeRemaining.days < 30 -> "${prefix}${timeRemaining.weeks}w ${timeRemaining.days % 7}d"
            else -> "${prefix}${timeRemaining.months}mo ${timeRemaining.days % 30}d"
        }
    }
}

data class FormattedTime(
    val primaryValue: String,
    val primaryUnit: String,
    val secondaryValue: String?,
    val secondaryUnit: String?
)
