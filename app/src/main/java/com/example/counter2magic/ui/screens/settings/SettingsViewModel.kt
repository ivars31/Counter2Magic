package com.example.counter2magic.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.counter2magic.domain.usecase.SyncCalendarEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val syncCalendarEventsUseCase: SyncCalendarEventsUseCase
) : ViewModel() {

    private val prefs = context.getSharedPreferences("counter2magic_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _uiState.value = SettingsUiState(
            calendarSyncEnabled = prefs.getBoolean(KEY_CALENDAR_SYNC, false),
            notificationsEnabled = prefs.getBoolean(KEY_NOTIFICATIONS, true)
        )
    }

    fun setCalendarSyncEnabled(enabled: Boolean) {
        viewModelScope.launch {
            prefs.edit().putBoolean(KEY_CALENDAR_SYNC, enabled).apply()
            _uiState.value = _uiState.value.copy(calendarSyncEnabled = enabled)

            if (enabled) {
                syncCalendar()
            }
        }
    }

    fun syncCalendar() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSyncing = true)
            try {
                val result = syncCalendarEventsUseCase()
                _uiState.value = _uiState.value.copy(
                    isSyncing = false,
                    lastSyncMessage = "Synced ${result.total} events (${result.added} new, ${result.updated} updated)"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSyncing = false,
                    lastSyncMessage = "Sync failed: ${e.message}"
                )
            }
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            prefs.edit().putBoolean(KEY_NOTIFICATIONS, enabled).apply()
            _uiState.value = _uiState.value.copy(notificationsEnabled = enabled)
        }
    }

    fun clearSyncMessage() {
        _uiState.value = _uiState.value.copy(lastSyncMessage = null)
    }

    companion object {
        private const val KEY_CALENDAR_SYNC = "calendar_sync_enabled"
        private const val KEY_NOTIFICATIONS = "notifications_enabled"
    }
}

data class SettingsUiState(
    val calendarSyncEnabled: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val isSyncing: Boolean = false,
    val lastSyncMessage: String? = null
)
