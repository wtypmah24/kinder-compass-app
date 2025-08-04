package com.example.school_companion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpecialNeed(
    val id: Long,
    val type: String,
    val description: String,
) : Parcelable
