package com.example.school_companion.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class AssistantAnswer(
    val id: String,
    val thread_id: String,
    val role: String,
    val message: String,
    val created_at: Long,
) : Parcelable
