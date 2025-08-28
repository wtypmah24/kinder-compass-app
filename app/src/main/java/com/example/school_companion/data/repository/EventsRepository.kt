package com.example.school_companion.data.repository

import com.example.school_companion.data.api.EventApi
import com.example.school_companion.data.api.EventRequestDto
import com.example.school_companion.data.model.Event
import javax.inject.Inject

class EventsRepository @Inject constructor(
    private val apiService: EventApi
) {
    suspend fun getEventsByCompanion(): Result<List<Event>> {
        return try {
            val response = apiService.getEventsByCompanion()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get events: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEventsByChild(childId: Long): Result<List<Event>> {
        return try {
            val response = apiService.getEventsByChild(childId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get events: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createEvent(childId: Long, event: EventRequestDto): Result<String> {
        return try {
            val response = apiService.addEvent(event, childId)
            if (response.isSuccessful) {
                Result.success(response.body()?.string() ?: "Event added successfully")
            } else {
                Result.failure(Exception("Failed to create event: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateEvent(childId: Long, eventId: Long, event: EventRequestDto): Result<String> {
        return try {
            val response = apiService.updateEvent(event, childId, eventId)
            if (response.isSuccessful) {
                Result.success(response.body()?.string() ?: "Event updated successfully")
            } else {
                Result.failure(Exception("Failed to update event: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteEvent(eventId: Long, childId: Long): Result<String> {
        return try {
            val response = apiService.delete(eventId, childId)
            if (response.isSuccessful) {
                Result.success(response.body()?.string() ?: "Event deleted successfully")
            } else {
                Result.failure(Exception("Failed to delete event: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
