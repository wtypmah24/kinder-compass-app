package com.example.school_companion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MonitoringEntry(
    val id: Long,
    val value: String,
    val notes: String,
    val parameterId: Long,
    val parameterName: String,
    val type: ScaleType,
    val childId: Long,
    val createdAt: String

) : Parcelable