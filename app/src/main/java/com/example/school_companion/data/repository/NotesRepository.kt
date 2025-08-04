package com.example.school_companion.data.repository

import com.example.school_companion.data.api.ApiService
import com.example.school_companion.data.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val apiService: ApiService
) {
    
    suspend fun getNotes(
        token: String,
        childId: String? = null
    ): Flow<Result<List<Note>>> = flow {
        try {
            val response = apiService.getNotes("Bearer $token", childId)
            if (response.isSuccessful) {
                response.body()?.let { notes ->
                    emit(Result.success(notes))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to get notes: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun createNote(token: String, note: Note): Flow<Result<Note>> = flow {
        try {
            val response = apiService.createNote("Bearer $token", note)
            if (response.isSuccessful) {
                response.body()?.let { createdNote ->
                    emit(Result.success(createdNote))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to create note: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun updateNote(token: String, noteId: String, note: Note): Flow<Result<Note>> = flow {
        try {
            val response = apiService.updateNote("Bearer $token", noteId, note)
            if (response.isSuccessful) {
                response.body()?.let { updatedNote ->
                    emit(Result.success(updatedNote))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to update note: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun deleteNote(token: String, noteId: String): Flow<Result<Unit>> = flow {
        try {
            val response = apiService.deleteNote("Bearer $token", noteId)
            if (response.isSuccessful) {
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(Exception("Failed to delete note: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
} 