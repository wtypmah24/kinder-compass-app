package com.example.school_companion.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Event(
    val id: Long,
    val title: String,
    val description: String,
    val startDateTime: String,
    val endDateTime: String,
    val location: String,
    val childId: Long
) : Parcelable
data class EventWithChild(
    val event: Event,
    val child: Child
)
