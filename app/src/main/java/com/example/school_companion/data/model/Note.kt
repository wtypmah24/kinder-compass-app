package com.example.school_companion.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Note(
    val id: String,
    val childId: String,
    val title: String,
    val content: String,
    val date: String,
    val category: NoteCategory = NoteCategory.GENERAL,
    val companionId: String
) : Parcelable

enum class NoteCategory {
    GENERAL, BEHAVIOR, ACADEMIC, SOCIAL, EMOTIONAL, MEDICAL, GOAL_PROGRESS
} 