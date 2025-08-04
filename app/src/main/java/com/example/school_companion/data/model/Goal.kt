package com.example.school_companion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Goal(
    val id: Long,
    val description: String,
) : Parcelable