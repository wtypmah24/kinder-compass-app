package com.example.school_companion.data.repository

import com.example.school_companion.data.api.EventApi
import com.example.school_companion.data.api.EventRequestDto
import com.example.school_companion.data.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EventsRepository @Inject constructor(
    private val apiService: EventApi
) {

    suspend fun getEventsByCompanion(
        token: String
    ): Flow<Result<List<Event>>> = flow {
        try {
            val response = apiService.getEventsByCompanion("Bearer $token")
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

    suspend fun getEventsByChild(
        token: String,
        childId: Long
    ): Flow<Result<List<Event>>> = flow {
        try {
            val response = apiService.getEventsByChild("Bearer $token", childId)
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

    suspend fun createEvent(
        token: String,
        childId: Long,
        event: EventRequestDto
    ) = flow {
        try {
            val response = apiService.addEvent("Bearer $token", event, childId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Event added successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to create event: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}