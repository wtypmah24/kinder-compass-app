package com.example.school_companion.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import java.time.LocalDateTime

@Parcelize
data class MonitoringEntry(
    val id: Long,
    val value: String,
    val notes: String,
    val parameterId: Long,
    val parameterName: String,
    val type: String,
    val childId: Long,
    val createdAt: String

) : Parcelable