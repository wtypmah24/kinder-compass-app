package com.example.school_companion.data.repository

import com.example.school_companion.data.api.EventApi
import com.example.school_companion.data.api.EventRequestDto
import com.example.school_companion.data.model.Event
import com.example.school_companion.ui.util.toResult
import com.example.school_companion.ui.util.toResultString
import javax.inject.Inject

class EventsRepository @Inject constructor(
    private val apiService: EventApi
) {
    suspend fun getEventsByCompanion(): Result<List<Event>> {
        return try {
            apiService.getEventsByCompanion().toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEventsByChild(childId: Long): Result<List<Event>> {
        return try {
            apiService.getEventsByChild(childId).toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createEvent(childId: Long, event: EventRequestDto): Result<String> {
        return try {
            apiService.addEvent(event, childId)
                .toResultString("Event added successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateEvent(childId: Long, eventId: Long, event: EventRequestDto): Result<String> {
        return try {
            apiService.updateEvent(event, childId, eventId)
                .toResultString("Event updated successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteEvent(eventId: Long, childId: Long): Result<String> {
        return try {
            apiService.delete(eventId, childId)
                .toResultString("Event deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
