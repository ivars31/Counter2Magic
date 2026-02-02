package com.example.counter2magic.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.counter2magic.domain.model.TimeRemaining

enum class EventUrgency {
    PAST,       // Passed - muted, reduced opacity
    IMMINENT,   // < 1 hour - red accent, pulsing dot
    TODAY,      // < 24 hours - amber accent
    SOON,       // < 7 days - blue (default)
    UPCOMING;   // > 7 days - muted gray

    @Composable
    fun color(): Color = when (this) {
        PAST -> PastMuted
        IMMINENT -> if (isSystemInDarkTheme()) UrgentRedDark else UrgentRed
        TODAY -> if (isSystemInDarkTheme()) WarningAmberDark else WarningAmber
        SOON -> if (isSystemInDarkTheme()) CountdownAccentDark else CountdownAccent
        UPCOMING -> UpcomingMuted
    }
}

fun TimeRemaining.toUrgency(): EventUrgency = when {
    isPast -> EventUrgency.PAST
    totalSeconds < 3600 -> EventUrgency.IMMINENT      // < 1 hour
    totalSeconds < 86400 -> EventUrgency.TODAY        // < 24 hours
    totalSeconds < 604800 -> EventUrgency.SOON        // < 7 days
    else -> EventUrgency.UPCOMING
}
