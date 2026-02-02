package com.example.counter2magic.domain.model

data class TimeRemaining(
    val totalSeconds: Long,
    val isPast: Boolean = totalSeconds < 0
) {
    val absoluteSeconds: Long = kotlin.math.abs(totalSeconds)

    val seconds: Long = absoluteSeconds % 60
    val minutes: Long = (absoluteSeconds / 60) % 60
    val hours: Long = (absoluteSeconds / 3600) % 24
    val days: Long = absoluteSeconds / 86400
    val weeks: Long = days / 7
    val months: Long = days / 30

    companion object {
        fun fromSeconds(seconds: Long) = TimeRemaining(seconds)
    }
}
