package com.example.school_companion.data.repository

import com.example.school_companion.data.api.ApiService
import com.example.school_companion.data.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EventsRepository @Inject constructor(
    private val apiService: ApiService
) {
    
    suspend fun getEvents(
        token: String,
        childId: String? = null,
        date: String? = null
    ): Flow<Result<List<Event>>> = flow {
        try {
            val response = apiService.getEvents("Bearer $token", childId, date)
            if (response.isSuccessful) {
                response.body()?.let { events ->
                    emit(Result.success(events))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to get events: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun createEvent(token: String, event: Event): Flow<Result<Event>> = flow {
        try {
            val response = apiService.createEvent("Bearer $token", event)
            if (response.isSuccessful) {
                response.body()?.let { createdEvent ->
                    emit(Result.success(createdEvent))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to create event: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun updateEvent(token: String, eventId: String, event: Event): Flow<Result<Event>> = flow {
        try {
            val response = apiService.updateEvent("Bearer $token", eventId, event)
            if (response.isSuccessful) {
                response.body()?.let { updatedEvent ->
                    emit(Result.success(updatedEvent))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to update event: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun deleteEvent(token: String, eventId: String): Flow<Result<Unit>> = flow {
        try {
            val response = apiService.deleteEvent("Bearer $token", eventId)
            if (response.isSuccessful) {
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(Exception("Failed to delete event: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
} 