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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventsRepository: EventsRepository,
    private val childrenRepository: ChildrenRepository
) : ViewModel() {

    private val _eventsState = MutableStateFlow<EventsState>(EventsState.Loading)
    val eventsState: StateFlow<EventsState> = _eventsState.asStateFlow()

    private val _eventsWithChildrenState =
        MutableStateFlow<EventsWithChildrenState>(EventsWithChildrenState.Loading)
    val eventsWithChildrenState: StateFlow<EventsWithChildrenState> =
        _eventsWithChildrenState.asStateFlow()

    fun loadEventsByCompanion() {
        viewModelScope.launch {
            _eventsState.value = EventsState.Loading
            val result = eventsRepository.getEventsByCompanion()
            result.fold(
                onSuccess = { _eventsState.value = EventsState.Success(it) },
                onFailure = {
                    _eventsState.value = EventsState.Error(it.message ?: "Failed to load events")
                }
            )
        }
    }

    fun loadEventsByChild(childId: Long) {
        viewModelScope.launch {
            _eventsState.value = EventsState.Loading
            val result = eventsRepository.getEventsByChild(childId)
            result.fold(
                onSuccess = { _eventsState.value = EventsState.Success(it) },
                onFailure = {
                    _eventsState.value = EventsState.Error(it.message ?: "Failed to load events")
                }
            )
        }
    }

    fun createEvent(childId: Long, event: EventRequestDto) {
        viewModelScope.launch {
            val result = eventsRepository.createEvent(childId, event)
            result.fold(
                onSuccess = { loadEventsByCompanion() },
                onFailure = {
                    _eventsState.value = EventsState.Error(it.message ?: "Failed to create event")
                    loadEventsByCompanion()
                }
            )
        }
    }

    fun updateEvent(childId: Long, eventId: Long, event: EventRequestDto) {
        viewModelScope.launch {
            val result = eventsRepository.updateEvent(childId, eventId, event)
            result.fold(
                onSuccess = { loadEventsByCompanion() },
                onFailure = {
                    _eventsState.value = EventsState.Error(it.message ?: "Failed to update event")
                    loadEventsByCompanion()
                }
            )
        }
    }

    fun deleteEvent(eventId: Long, childId: Long) {
        viewModelScope.launch {
            val result = eventsRepository.deleteEvent(eventId, childId)
            result.fold(
                onSuccess = { loadEventsByCompanion() },
                onFailure = {
                    _eventsState.value = EventsState.Error(it.message ?: "Failed to delete event")
                    loadEventsByCompanion()
                }
            )
        }
    }

    fun loadEventsWithChildren() {
        viewModelScope.launch {
            _eventsWithChildrenState.value = EventsWithChildrenState.Loading

            try {
                val eventsResult = eventsRepository.getEventsByCompanion()
                if (eventsResult.isFailure) {
                    _eventsWithChildrenState.value =
                        EventsWithChildrenState.Error(
                            eventsResult.exceptionOrNull()?.message ?: "Failed to load events"
                        )
                    return@launch
                }

                val events = eventsResult.getOrThrow()
                if (events.isEmpty()) {
                    _eventsWithChildrenState.value = EventsWithChildrenState.Success(emptyList())
                    return@launch
                }

                val childIds = events.map { it.childId }.distinct()
                val children = childIds.mapNotNull { id ->
                    childrenRepository.getChild(id).getOrNull()
                }

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