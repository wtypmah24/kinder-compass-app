package com.example.school_companion.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Child(
    val id: Long,
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String,
    val dateOfBirth: String,
    val active: Boolean
) : Parcelable