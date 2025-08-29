package com.example.school_companion.data.repository

import com.example.school_companion.data.api.NoteApi
import com.example.school_companion.data.api.NoteRequestDto
import com.example.school_companion.data.model.Note
import com.example.school_companion.ui.util.toResult
import com.example.school_companion.ui.util.toResultString
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val apiService: NoteApi
) {
    suspend fun getNotes(childId: Long): Result<List<Note>> {
        return try {
            apiService.getNotesByChild(childId).toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createNote(note: NoteRequestDto, childId: Long): Result<String> {
        return try {
            apiService.addNote(note, childId)
                .toResultString("Note added successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateNote(noteId: Long, note: NoteRequestDto, childId: Long): Result<String> {
        return try {
            apiService.updateNote(note, childId, noteId)
                .toResultString("Note updated successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteNote(noteId: Long): Result<String> {
        return try {
            apiService.delete(noteId)
                .toResultString("Note deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
