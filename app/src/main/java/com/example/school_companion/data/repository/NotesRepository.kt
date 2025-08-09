package com.example.school_companion.data.repository

import com.example.school_companion.data.api.ApiService
import com.example.school_companion.data.api.NoteApi
import com.example.school_companion.data.api.NoteRequestDto
import com.example.school_companion.data.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val apiService: NoteApi
) {

    suspend fun getNotes(
        token: String,
        childId: Long
    ): Flow<Result<List<Note>>> = flow {
        try {
            val response = apiService.getNotesByChild("Bearer $token", childId)
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

    suspend fun createNote(token: String, note: NoteRequestDto, childId: Long) =
        flow {
            try {
                val response = apiService.addNote("Bearer $token", note, childId)
                if (response.isSuccessful) {
                    val message = response.body()?.string() ?: "Note added successfully"
                    emit(Result.success(message))
                } else {
                    emit(Result.failure(Exception("Failed to create note: ${response.code()}")))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }

    suspend fun updateNote(
        token: String,
        noteId: Long,
        note: NoteRequestDto,
        childId: Long
    ) = flow {
        try {
            val response = apiService.updateNote("Bearer $token", note, childId, noteId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Note updated successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to update note: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun deleteNote(token: String, noteId: Long) = flow {
        try {
            val response = apiService.delete("Bearer $token", noteId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Note deleted successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to delete note: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
} 