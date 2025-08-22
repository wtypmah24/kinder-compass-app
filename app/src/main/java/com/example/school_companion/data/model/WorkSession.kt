package com.example.school_companion.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class WorkSession(
    val id: Long,
    val startTime: String,
    val endTime: String?,
    val note: String?,
) : Parcelable 