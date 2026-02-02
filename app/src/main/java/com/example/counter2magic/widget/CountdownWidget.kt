package com.example.counter2magic.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.counter2magic.data.local.db.CountdownDatabase
import com.example.counter2magic.domain.formatter.AdaptiveTimeFormatter
import com.example.counter2magic.domain.model.TimeRemaining
import kotlinx.coroutines.flow.first
import java.time.Instant

class CountdownWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val database = CountdownDatabase.getInstance(context)
        val events = database.eventDao()
            .getUpcomingEventsLimit(System.currentTimeMillis(), 3)
            .first()

        val formatter = AdaptiveTimeFormatter()
        val now = Instant.now()

        val eventData = events.map { entity ->
            val event = entity.toDomain()
            val seconds = event.targetDateTime.epochSecond - now.epochSecond
            val timeRemaining = TimeRemaining.fromSeconds(seconds)
            val compact = formatter.formatCompact(timeRemaining)
            WidgetEventData(
                id = event.id,
                title = event.title,
                countdown = compact
            )
        }

        provideContent {
            GlanceTheme {
                WidgetContent(events = eventData)
            }
        }
    }
}

data class WidgetEventData(
    val id: Long,
    val title: String,
    val countdown: String
)

@Composable
private fun WidgetContent(events: List<WidgetEventData>) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.surface)
            .padding(12.dp)
            .clickable(actionRunCallback<OpenAppAction>()),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Countdowns",
            style = TextStyle(
                color = GlanceTheme.colors.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        )

        Spacer(modifier = GlanceModifier.height(8.dp))

        if (events.isEmpty()) {
            Text(
                text = "No upcoming events",
                style = TextStyle(
                    color = GlanceTheme.colors.onSurfaceVariant,
                    fontSize = 12.sp
                )
            )
        } else {
            events.forEach { event ->
                WidgetEventRow(event)
                Spacer(modifier = GlanceModifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun WidgetEventRow(event: WidgetEventData) {
    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = event.title,
            style = TextStyle(
                color = GlanceTheme.colors.onSurface,
                fontSize = 13.sp
            ),
            maxLines = 1,
            modifier = GlanceModifier.defaultWeight()
        )

        Spacer(modifier = GlanceModifier.width(8.dp))

        Text(
            text = event.countdown,
            style = TextStyle(
                color = GlanceTheme.colors.primary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

class OpenAppAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.let { context.startActivity(it) }
    }
}
