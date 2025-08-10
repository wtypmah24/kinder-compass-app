package com.example.school_companion.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.api.EventRequestDto
import com.example.school_companion.data.model.Event
import com.example.school_companion.data.model.EventWithChild
import com.example.school_companion.data.repository.ChildrenRepository
import com.example.school_companion.data.repository.EventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventsRepository: EventsRepository,
    private val childrenRepository: ChildrenRepository
) : ViewModel() {

    private val _eventsState = MutableStateFlow<EventsState>(EventsState.Loading)
    val eventsState: StateFlow<EventsState> = _eventsState.asStateFlow()

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent.asStateFlow()

    private val _eventsWithChildrenState =
        MutableStateFlow<EventsWithChildrenState>(EventsWithChildrenState.Loading)
    val eventsWithChildrenState: StateFlow<EventsWithChildrenState> =
        _eventsWithChildrenState.asStateFlow()

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

    fun loadEventsWithChildren(token: String) {
        viewModelScope.launch {
            _eventsWithChildrenState.value = EventsWithChildrenState.Loading

            try {
                val eventsResult = eventsRepository.getEventsByCompanion(token).first()

                if (eventsResult.isFailure) {
                    val errorMsg =
                        eventsResult.exceptionOrNull()?.message ?: "Failed to load events"
                    _eventsWithChildrenState.value = EventsWithChildrenState.Error(errorMsg)
                    return@launch
                }

                val events = eventsResult.getOrThrow()

                if (events.isEmpty()) {
                    _eventsWithChildrenState.value = EventsWithChildrenState.Success(emptyList())
                    return@launch
                }

                val childIds = events.map { it.childId }.distinct()

                val children = childIds.map { id ->
                    async {
                        try {
                            val childResult = childrenRepository.getChild(token, id).first()
                            childResult.getOrNull()
                        } catch (e: Exception) {
                            null
                        }
                    }
                }.awaitAll().filterNotNull()

                val mapped = events.mapNotNull { event ->
                    val child = children.find { it.id == event.childId }
                    child?.let { EventWithChild(event, it) }
                }

                _eventsWithChildrenState.value = EventsWithChildrenState.Success(mapped)

            } catch (e: Exception) {
                Log.e("EventsVM", "Exception in loadEventsWithChildren: ${e.message}", e)
                _eventsWithChildrenState.value =
                    EventsWithChildrenState.Error(e.message ?: "Unknown error")
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

sealed class EventsWithChildrenState {
    data object Loading : EventsWithChildrenState()
    data class Success(val events: List<EventWithChild>) : EventsWithChildrenState()
    data class Error(val message: String) : EventsWithChildrenState()
}