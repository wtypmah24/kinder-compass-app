package com.example.school_companion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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