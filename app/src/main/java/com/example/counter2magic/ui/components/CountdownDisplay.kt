package com.example.counter2magic.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.counter2magic.domain.formatter.FormattedTime
import com.example.counter2magic.domain.model.TimeRemaining
import com.example.counter2magic.ui.theme.CountdownAccent
import com.example.counter2magic.ui.theme.CountdownAccentDark

@Composable
fun AnimatedCountdownDigit(
    digit: Char,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = digit,
        transitionSpec = {
            (slideInVertically { -it } + fadeIn()) togetherWith
            (slideOutVertically { it } + fadeOut())
        },
        label = "digit_animation",
        modifier = modifier
    ) { targetDigit ->
        Text(
            text = targetDigit.toString(),
            style = style,
            color = color,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun AnimatedCountdownValue(
    value: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        value.forEach { char ->
            if (char.isDigit()) {
                AnimatedCountdownDigit(
                    digit = char,
                    style = style,
                    color = color
                )
            } else {
                Text(
                    text = char.toString(),
                    style = style,
                    color = color,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Composable
fun CountdownDisplay(
    formattedTime: FormattedTime,
    modifier: Modifier = Modifier,
    size: CountdownSize = CountdownSize.LARGE,
    accentColorOverride: Color? = null
) {
    val defaultAccentColor = if (isSystemInDarkTheme()) CountdownAccentDark else CountdownAccent
    val accentColor = accentColorOverride ?: defaultAccentColor

    val textStyle = when (size) {
        CountdownSize.LARGE -> MaterialTheme.typography.displayLarge
        CountdownSize.MEDIUM -> MaterialTheme.typography.displayMedium
        CountdownSize.SMALL -> MaterialTheme.typography.displaySmall
        CountdownSize.COMPACT -> MaterialTheme.typography.headlineLarge
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start
    ) {
        // Primary value with animated digits
        AnimatedCountdownValue(
            value = formattedTime.primaryValue,
            style = textStyle,
            color = accentColor
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = formattedTime.primaryUnit,
                style = when (size) {
                    CountdownSize.LARGE -> MaterialTheme.typography.titleLarge
                    CountdownSize.MEDIUM -> MaterialTheme.typography.titleMedium
                    CountdownSize.SMALL -> MaterialTheme.typography.titleSmall
                    CountdownSize.COMPACT -> MaterialTheme.typography.bodyMedium
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Secondary value if present
            if (formattedTime.secondaryValue != null && formattedTime.secondaryUnit != null) {
                Text(
                    text = "${formattedTime.secondaryValue} ${formattedTime.secondaryUnit}",
                    style = when (size) {
                        CountdownSize.LARGE -> MaterialTheme.typography.bodyLarge
                        CountdownSize.MEDIUM -> MaterialTheme.typography.bodyMedium
                        CountdownSize.SMALL -> MaterialTheme.typography.bodySmall
                        CountdownSize.COMPACT -> MaterialTheme.typography.labelSmall
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun SegmentedCountdownDisplay(
    timeRemaining: TimeRemaining,
    modifier: Modifier = Modifier,
    accentColorOverride: Color? = null
) {
    val defaultAccentColor = if (isSystemInDarkTheme()) CountdownAccentDark else CountdownAccent
    val accentColor = accentColorOverride ?: defaultAccentColor

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (timeRemaining.days > 0) {
            TimeSegment(
                value = timeRemaining.days,
                label = if (timeRemaining.days == 1L) "day" else "days",
                accentColor = accentColor
            )
        }
        TimeSegment(
            value = timeRemaining.hours,
            label = if (timeRemaining.hours == 1L) "hour" else "hours",
            accentColor = accentColor
        )
        TimeSegment(
            value = timeRemaining.minutes,
            label = if (timeRemaining.minutes == 1L) "min" else "min",
            accentColor = accentColor
        )
        TimeSegment(
            value = timeRemaining.seconds,
            label = "sec",
            accentColor = accentColor
        )
    }
}

@Composable
private fun TimeSegment(
    value: Long,
    label: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp)
        ) {
            AnimatedCountdownValue(
                value = value.toString().padStart(2, '0'),
                style = MaterialTheme.typography.displaySmall,
                color = accentColor,
                modifier = Modifier.padding(12.dp, 8.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

enum class CountdownSize {
    LARGE,
    MEDIUM,
    SMALL,
    COMPACT
}
