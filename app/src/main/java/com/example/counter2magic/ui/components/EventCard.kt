package com.example.counter2magic.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.counter2magic.domain.formatter.FormattedTime
import com.example.counter2magic.domain.model.CountdownEvent
import com.example.counter2magic.domain.model.TimeRemaining
import com.example.counter2magic.ui.theme.EventUrgency
import com.example.counter2magic.ui.theme.toUrgency
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun UrgencyDot(
    urgency: EventUrgency,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "urgency_pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "urgency_alpha"
    )

    val urgencyColor = urgency.color()

    Box(
        modifier = modifier
            .size(8.dp)
            .background(
                color = urgencyColor.copy(
                    alpha = if (urgency == EventUrgency.IMMINENT) alpha else 1f
                ),
                shape = CircleShape
            )
    )
}

@Composable
fun EventCard(
    event: CountdownEvent,
    formattedTime: FormattedTime,
    timeRemaining: TimeRemaining,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a")
        .withZone(ZoneId.systemDefault())

    val urgency = timeRemaining.toUrgency()
    val urgencyColor = urgency.color()
    val cardAlpha = if (urgency == EventUrgency.PAST) 0.6f else 1f

    val borderModifier = if (urgency == EventUrgency.IMMINENT) {
        Modifier.border(
            width = 1.dp,
            color = urgencyColor.copy(alpha = 0.5f),
            shape = RoundedCornerShape(12.dp)
        )
    } else {
        Modifier
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(borderModifier)
            .alpha(cardAlpha)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Countdown display with urgency color
            CountdownDisplay(
                formattedTime = formattedTime,
                size = CountdownSize.MEDIUM,
                accentColorOverride = urgencyColor
            )

            Spacer(modifier = Modifier.width(20.dp))

            // Right side: Event details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Event title with urgency dot and calendar icon
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UrgencyDot(urgency = urgency)
                    Spacer(modifier = Modifier.width(8.dp))
                    if (event.isFromCalendar) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "From calendar",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Event title - more prominent
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Target date
                Text(
                    text = dateFormatter.format(event.targetDateTime),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CompactEventCard(
    event: CountdownEvent,
    formattedTime: FormattedTime,
    timeRemaining: TimeRemaining,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val urgency = timeRemaining.toUrgency()
    val urgencyColor = urgency.color()
    val cardAlpha = if (urgency == EventUrgency.PAST) 0.6f else 1f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .alpha(cardAlpha)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UrgencyDot(urgency = urgency)
            Spacer(modifier = Modifier.width(8.dp))

            // Event title
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (event.isFromCalendar) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "From calendar",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Countdown with urgency color
            CountdownDisplay(
                formattedTime = formattedTime,
                size = CountdownSize.COMPACT,
                accentColorOverride = urgencyColor
            )
        }
    }
}
