package com.example.school_companion.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Task(
    val id: Long,
    val title: String,
    val description: String,
    val status: String,
    val deadLine: String,
) : Parcelable