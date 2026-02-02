package com.example.counter2magic.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.counter2magic.data.repository.EventRepository
import com.example.counter2magic.domain.formatter.AdaptiveTimeFormatter
import com.example.counter2magic.domain.formatter.FormattedTime
import com.example.counter2magic.domain.model.CountdownEvent
import com.example.counter2magic.domain.usecase.CalculateTimeRemainingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val calculateTimeRemaining: CalculateTimeRemainingUseCase,
    private val timeFormatter: AdaptiveTimeFormatter
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
        startCountdownTimer()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            eventRepository.getUpcomingEvents().collect { events ->
                updateEventsWithFormattedTime(events)
            }
        }
    }

    private fun startCountdownTimer() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                val currentEvents = _uiState.value.events.map { it.event }
                updateEventsWithFormattedTime(currentEvents)
            }
        }
    }

    private fun updateEventsWithFormattedTime(events: List<CountdownEvent>) {
        val now = Instant.now()
        val eventsWithTime = events.map { event ->
            val timeRemaining = calculateTimeRemaining(event, now)
            val formattedTime = timeFormatter.format(timeRemaining)
            EventWithFormattedTime(event, formattedTime, timeRemaining.isPast)
        }
        _uiState.value = _uiState.value.copy(
            events = eventsWithTime,
            isLoading = false
        )
    }
}

data class HomeUiState(
    val events: List<EventWithFormattedTime> = emptyList(),
    val isLoading: Boolean = true
)

data class EventWithFormattedTime(
    val event: CountdownEvent,
    val formattedTime: FormattedTime,
    val isPast: Boolean
)
