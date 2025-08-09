package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.api.EventRequestDto
import com.example.school_companion.data.model.Event
import com.example.school_companion.data.repository.EventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventsRepository: EventsRepository
) : ViewModel() {

    private val _eventsState = MutableStateFlow<EventsState>(EventsState.Loading)
    val eventsState: StateFlow<EventsState> = _eventsState.asStateFlow()

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent.asStateFlow()

    fun loadEventsByCompanion(token: String) {
        viewModelScope.launch {
            _eventsState.value = EventsState.Loading

            eventsRepository.getEventsByCompanion(token).collect { result ->
                result.fold(
                    onSuccess = { events ->
                        _eventsState.value = EventsState.Success(events)
                    },
                    onFailure = { exception ->
                        _eventsState.value =
                            EventsState.Error(exception.message ?: "Failed to load events")
                    }
                )
            }
        }
    }

    fun loadEventsByChild(token: String, childId: Long) {
        viewModelScope.launch {
            _eventsState.value = EventsState.Loading

            eventsRepository.getEventsByChild(token, childId).collect { result ->
                result.fold(
                    onSuccess = { events ->
                        _eventsState.value = EventsState.Success(events)
                    },
                    onFailure = { exception ->
                        _eventsState.value =
                            EventsState.Error(exception.message ?: "Failed to load events")
                    }
                )
            }
        }
    }

    fun createEvent(token: String, childId: Long, event: EventRequestDto) {
        viewModelScope.launch {
            eventsRepository.createEvent(token, childId, event).collect { result ->
                result.fold(
                    onSuccess = {
                        // Refresh events list
                        loadEventsByCompanion(token)
                    },
                    onFailure = { exception ->
                        _eventsState.value =
                            EventsState.Error(exception.message ?: "Failed to create event")
                        loadEventsByCompanion(token)
                    }
                )
            }
        }
    }

    fun updateEvent(token: String, childId: Long, eventId: Long, event: EventRequestDto) {
        viewModelScope.launch {
            eventsRepository.updateEvent(token, childId, eventId, event).collect { result ->
                result.fold(
                    onSuccess = {
                        // Refresh events list
                        loadEventsByCompanion(token)
                    },
                    onFailure = { exception ->
                        _eventsState.value =
                            EventsState.Error(exception.message ?: "Failed to create event")
                        loadEventsByCompanion(token)
                    }
                )
            }
        }
    }

    fun deleteEvent(token: String, eventId: Long, childId: Long) {
        viewModelScope.launch {
            eventsRepository.deleteEvent(token, eventId, childId).collect { result ->
                result.fold(
                    onSuccess = {
                        // Refresh events list
                        loadEventsByCompanion(token)
                    },
                    onFailure = { exception ->
                        _eventsState.value =
                            EventsState.Error(exception.message ?: "Failed to create event")
                        loadEventsByCompanion(token)
                    }
                )
            }
        }
    }

    fun selectEvent(event: Event) {
        _selectedEvent.value = event
    }

    fun clearSelectedEvent() {
        _selectedEvent.value = null
    }
}

sealed class EventsState {
    data object Loading : EventsState()
    data class Success(val events: List<Event>) : EventsState()
    data class Error(val message: String) : EventsState()
} 