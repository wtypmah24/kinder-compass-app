package com.example.school_companion.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Photo(
    val id: String,
    val description: String,
    val createdAt: String
) : Parcelable