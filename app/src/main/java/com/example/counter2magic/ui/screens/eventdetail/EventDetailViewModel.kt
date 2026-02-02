package com.example.counter2magic.ui.screens.eventdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.counter2magic.data.repository.EventRepository
import com.example.counter2magic.data.repository.ReminderRepository
import com.example.counter2magic.domain.formatter.AdaptiveTimeFormatter
import com.example.counter2magic.domain.formatter.FormattedTime
import com.example.counter2magic.domain.model.CountdownEvent
import com.example.counter2magic.domain.model.Reminder
import com.example.counter2magic.domain.model.ReminderOffset
import com.example.counter2magic.domain.usecase.CalculateTimeRemainingUseCase
import com.example.counter2magic.notification.ReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val eventRepository: EventRepository,
    private val reminderRepository: ReminderRepository,
    private val reminderScheduler: ReminderScheduler,
    private val calculateTimeRemaining: CalculateTimeRemainingUseCase,
    private val timeFormatter: AdaptiveTimeFormatter
) : ViewModel() {

    private val eventId: Long = checkNotNull(savedStateHandle["eventId"])

    private val _uiState = MutableStateFlow(EventDetailUiState())
    val uiState: StateFlow<EventDetailUiState> = _uiState.asStateFlow()

    init {
        loadEvent()
        loadReminders()
        startCountdownTimer()
    }

    private fun loadEvent() {
        viewModelScope.launch {
            val event = eventRepository.getEventById(eventId)
            if (event != null) {
                updateEventWithFormattedTime(event)
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Event not found")
            }
        }
    }

    private fun loadReminders() {
        viewModelScope.launch {
            reminderRepository.getRemindersForEvent(eventId).collect { reminders ->
                _uiState.value = _uiState.value.copy(reminders = reminders)
            }
        }
    }

    private fun startCountdownTimer() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.value.event?.let { event ->
                    updateEventWithFormattedTime(event)
                }
            }
        }
    }

    private fun updateEventWithFormattedTime(event: CountdownEvent) {
        val timeRemaining = calculateTimeRemaining(event, Instant.now())
        val formattedTime = timeFormatter.format(timeRemaining)
        _uiState.value = _uiState.value.copy(
            event = event,
            formattedTime = formattedTime,
            isPast = timeRemaining.isPast,
            isLoading = false
        )
    }

    fun addReminder(offset: ReminderOffset) {
        val event = _uiState.value.event ?: return

        viewModelScope.launch {
            val triggerTime = event.targetDateTime.minus(offset.duration)
            val reminder = Reminder(
                eventId = eventId,
                triggerTime = triggerTime,
                offsetBeforeEvent = offset.duration
            )
            reminderRepository.insertReminder(reminder)
            reminderScheduler.scheduleReminder(reminder, event.title)
        }
    }

    fun removeReminder(reminder: Reminder) {
        viewModelScope.launch {
            reminderRepository.deleteReminderById(reminder.id)
            reminderScheduler.cancelReminder(reminder.id)
        }
    }

    fun deleteEvent(onDeleted: () -> Unit) {
        viewModelScope.launch {
            // Cancel all reminders for this event
            _uiState.value.reminders.forEach { reminder ->
                reminderScheduler.cancelReminder(reminder.id)
            }
            reminderRepository.deleteRemindersForEvent(eventId)
            eventRepository.deleteEventById(eventId)
            onDeleted()
        }
    }
}

data class EventDetailUiState(
    val event: CountdownEvent? = null,
    val formattedTime: FormattedTime? = null,
    val isPast: Boolean = false,
    val reminders: List<Reminder> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
