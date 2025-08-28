package com.example.school_companion.data.repository

import com.example.school_companion.data.api.NoteApi
import com.example.school_companion.data.api.NoteRequestDto
import com.example.school_companion.data.model.Note
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val apiService: NoteApi
) {
    suspend fun getNotes(childId: Long): Result<List<Note>> {
        return try {
            val response = apiService.getNotesByChild(childId)
            if (response.isSuccessful) {
                response.body()?.let { notes ->
                    Result.success(notes)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get notes: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createNote(note: NoteRequestDto, childId: Long): Result<String> {
        return try {
            val response = apiService.addNote(note, childId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Note added successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to create note: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateNote(noteId: Long, note: NoteRequestDto, childId: Long): Result<String> {
        return try {
            val response = apiService.updateNote(note, childId, noteId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Note updated successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to update note: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteNote(noteId: Long): Result<String> {
        return try {
            val response = apiService.delete(noteId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Note deleted successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to delete note: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
