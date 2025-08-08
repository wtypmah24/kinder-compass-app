package com.example.school_companion.data.repository

import com.example.school_companion.data.api.GoalApi
import com.example.school_companion.data.model.Goal
import com.example.school_companion.data.model.Note
import com.example.school_companion.data.model.SpecialNeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GoalsRepository @Inject constructor(
    private val apiService: GoalApi
) {

    suspend fun getGoals(
        token: String,
        childId: Long
    ): Flow<Result<List<Goal>>> = flow {
        try {
            val response = apiService.getGoalsByChild("Bearer $token", childId)
            if (response.isSuccessful) {
                response.body()?.let { goals ->
                    emit(Result.success(goals))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to get notes: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

//    suspend fun createNote(token: String, note: Note): Flow<Result<Note>> = flow {
//        try {
//            val response = apiService.createNote("Bearer $token", note)
//            if (response.isSuccessful) {
//                response.body()?.let { createdNote ->
//                    emit(Result.success(createdNote))
//                } ?: emit(Result.failure(Exception("Empty response")))
//            } else {
//                emit(Result.failure(Exception("Failed to create note: ${response.code()}")))
//            }
//        } catch (e: Exception) {
//            emit(Result.failure(e))
//        }
//    }

//    suspend fun updateNote(token: String, noteId: String, note: Note): Flow<Result<Note>> = flow {
//        try {
//            val response = apiService.updateNote("Bearer $token", noteId, note)
//            if (response.isSuccessful) {
//                response.body()?.let { updatedNote ->
//                    emit(Result.success(updatedNote))
//                } ?: emit(Result.failure(Exception("Empty response")))
//            } else {
//                emit(Result.failure(Exception("Failed to update note: ${response.code()}")))
//            }
//        } catch (e: Exception) {
//            emit(Result.failure(e))
//        }
//    }

//    suspend fun deleteNote(token: String, noteId: String): Flow<Result<Unit>> = flow {
//        try {
//            val response = apiService.deleteNote("Bearer $token", noteId)
//            if (response.isSuccessful) {
//                emit(Result.success(Unit))
//            } else {
//                emit(Result.failure(Exception("Failed to delete note: ${response.code()}")))
//            }
//        } catch (e: Exception) {
//            emit(Result.failure(e))
//        }
//    }
} 