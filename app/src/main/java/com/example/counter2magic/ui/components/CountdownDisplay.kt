package com.example.counter2magic.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.counter2magic.domain.formatter.FormattedTime
import com.example.counter2magic.ui.theme.CountdownAccent
import com.example.counter2magic.ui.theme.CountdownAccentDark
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
fun CountdownDisplay(
    formattedTime: FormattedTime,
    modifier: Modifier = Modifier,
    size: CountdownSize = CountdownSize.LARGE
) {
    val accentColor = if (isSystemInDarkTheme()) CountdownAccentDark else CountdownAccent

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start
    ) {
        // Primary value and unit
        Text(
            text = formattedTime.primaryValue,
            style = when (size) {
                CountdownSize.LARGE -> MaterialTheme.typography.displayLarge
                CountdownSize.MEDIUM -> MaterialTheme.typography.displayMedium
                CountdownSize.SMALL -> MaterialTheme.typography.displaySmall
                CountdownSize.COMPACT -> MaterialTheme.typography.headlineLarge
            },
            color = accentColor,
            fontWeight = FontWeight.Light
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

enum class CountdownSize {
    LARGE,
    MEDIUM,
    SMALL,
    COMPACT
}
