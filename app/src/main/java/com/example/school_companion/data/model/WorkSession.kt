package com.example.school_companion.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class WorkSession(
    val id: String,
    val companionId: String,
    val startTime: String,
    val endTime: String? = null,
    val duration: Long = 0, // in minutes
    val isActive: Boolean = true
) : Parcelable 