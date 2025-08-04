package com.example.school_companion.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Event(
    val id: Long,
    val title: String,
    val description: String,
    val startDateTime: String,
    val endDateTIme: String,
    val location: String,
) : Parcelable


