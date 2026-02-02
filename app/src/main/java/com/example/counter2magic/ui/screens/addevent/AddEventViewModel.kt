package com.example.counter2magic.ui.screens.addevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.counter2magic.data.repository.EventRepository
import com.example.counter2magic.domain.model.CountdownEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class AddEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEventUiState())
    val uiState: StateFlow<AddEventUiState> = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun updateDate(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
    }

    fun updateTime(time: LocalTime) {
        _uiState.value = _uiState.value.copy(selectedTime = time)
    }

    fun saveEvent(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (state.title.isBlank()) {
            _uiState.value = state.copy(titleError = "Title is required")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true)

            val dateTime = state.selectedDate.atTime(state.selectedTime)
                .atZone(ZoneId.systemDefault())
                .toInstant()

            val event = CountdownEvent(
                title = state.title.trim(),
                description = state.description.trim(),
                targetDateTime = dateTime
            )

            eventRepository.insertEvent(event)
            onSuccess()
        }
    }
}

data class AddEventUiState(
    val title: String = "",
    val description: String = "",
    val selectedDate: LocalDate = LocalDate.now().plusDays(1),
    val selectedTime: LocalTime = LocalTime.NOON,
    val titleError: String? = null,
    val isSaving: Boolean = false
)
