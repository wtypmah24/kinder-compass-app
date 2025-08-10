package com.example.school_companion.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Companion(
    val id: Long,
    val email: String,
    val name: String,
    val surname: String,
    val avatarId: String? = null,
    val organization: String? = null,
    val startWorkingTime: String? = null,
    val endWorkingTime: String? = null
) : Parcelable