package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    
    fun loadEvents(token: String, childId: String? = null, date: String? = null) {
        viewModelScope.launch {
            _eventsState.value = EventsState.Loading
            
            eventsRepository.getEvents(token, childId, date).collect { result ->
                result.fold(
                    onSuccess = { events ->
                        _eventsState.value = EventsState.Success(events)
                    },
                    onFailure = { exception ->
                        _eventsState.value = EventsState.Error(exception.message ?: "Failed to load events")
                    }
                )
            }
        }
    }
    
    fun createEvent(token: String, event: Event) {
        viewModelScope.launch {
            eventsRepository.createEvent(token, event).collect { result ->
                result.fold(
                    onSuccess = { createdEvent ->
                        // Refresh events list
                        loadEvents(token)
                    },
                    onFailure = { exception ->
                        _eventsState.value = EventsState.Error(exception.message ?: "Failed to create event")
                    }
                )
            }
        }
    }
    
    fun updateEvent(token: String, eventId: String, event: Event) {
        viewModelScope.launch {
            eventsRepository.updateEvent(token, eventId, event).collect { result ->
                result.fold(
                    onSuccess = { updatedEvent ->
                        // Refresh events list
                        loadEvents(token)
                    },
                    onFailure = { exception ->
                        _eventsState.value = EventsState.Error(exception.message ?: "Failed to update event")
                    }
                )
            }
        }
    }
    
    fun deleteEvent(token: String, eventId: String) {
        viewModelScope.launch {
            eventsRepository.deleteEvent(token, eventId).collect { result ->
                result.fold(
                    onSuccess = {
                        // Refresh events list
                        loadEvents(token)
                    },
                    onFailure = { exception ->
                        _eventsState.value = EventsState.Error(exception.message ?: "Failed to delete event")
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
    object Loading : EventsState()
    data class Success(val events: List<Event>) : EventsState()
    data class Error(val message: String) : EventsState()
} 