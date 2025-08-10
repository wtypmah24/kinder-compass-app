package com.example.school_companion.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import java.time.LocalDateTime

@Parcelize
data class MonitoringParam(
    val id: Long,
    val title: String,
    val type: ScaleType,
    val description: String,
    val minValue: Int,
    val maxValue: Int,
    val createdAt: String
) : Parcelable

enum class ScaleType {
    QUANTITATIVE,
    QUALITATIVE,
    BINARY,
    SCALE;
}