package com.example.school_companion.ui.chart

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.school_companion.data.model.MonitoringEntry
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LineChart(entries: List<MonitoringEntry>, range: String) {
    if (entries.isEmpty()) {
        Text("No data")
        return
    }

    val lineColor = MaterialTheme.colorScheme.primary
    val textMeasurer = rememberTextMeasurer()
    val timeFormatterForLastDay = DateTimeFormatter.ofPattern("HH:mm")

    val filtered = filterByRange(range, entries)
    if (filtered.isEmpty()) {
        Text("No data for selected range")
        return
    }

    val points = createPointsFromEntries(filtered, range)
    if (points.isEmpty()) {
        Text("No numeric data to plot after grouping")
        return
    }

    val minValue = points.minOf { it.second }
    val maxValue = points.maxOf { it.second }
    val minTime = points.minOf { it.first }
    val maxTime = points.maxOf { it.first }

    val xRange = (maxTime - minTime).takeIf { it != 0L } ?: 1L
    val yRange = (maxValue - minValue).takeIf { it != 0f } ?: 1f

    val padding = 40.dp
    val pointRadius = 4.dp

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        val pxPadding = padding.toPx()
        val chartWidth = size.width - pxPadding * 2
        val chartHeight = size.height - pxPadding * 2
        val maxYBound = maxOf(0f, size.height - 10f)

        val xScale =
            chartWidth / (if (range == "Last Day") (points.size - 1).coerceAtLeast(1).toFloat()
            else xRange.toFloat())
        val yScale = chartHeight / yRange

        drawLine(
            Color.Gray,
            Offset(pxPadding, size.height - pxPadding),
            Offset(size.width - pxPadding, size.height - pxPadding),
            1.dp.toPx()
        )
        drawLine(
            Color.Gray,
            Offset(pxPadding, pxPadding),
            Offset(pxPadding, size.height - pxPadding),
            1.dp.toPx()
        )

        val yLabelCount = 5
        val yStep = yRange / yLabelCount
        for (i in 0..yLabelCount) {
            val value = minValue + i * yStep
            val y = size.height - pxPadding - (value - minValue) * yScale
            val safeY = y.coerceIn(0f, maxYBound)

            drawText(
                textMeasurer = textMeasurer,
                text = String.format("%.1f", value),
                topLeft = Offset(4f, safeY - 8f),
                style = TextStyle(fontSize = 10.sp, color = Color.Gray)
            )

            drawLine(
                color = Color.LightGray.copy(alpha = 0.3f),
                start = Offset(pxPadding, safeY),
                end = Offset(size.width - pxPadding, safeY),
                strokeWidth = 0.5.dp.toPx()
            )
        }

        if (range == "Last Day") {
            points.forEachIndexed { index, (time, _) ->
                val x = pxPadding + index.toFloat() * xScale
                val label = try {
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
                        .format(timeFormatterForLastDay)
                } catch (_: Exception) {
                    ""
                }
                val labelY = (size.height - pxPadding + 4.dp.toPx()).coerceIn(0f, maxYBound)
                val safeX = (x - 10f).coerceAtLeast(0f)

                drawText(
                    textMeasurer = textMeasurer,
                    text = label,
                    topLeft = Offset(safeX, labelY),
                    style = TextStyle(fontSize = 10.sp, color = Color.Gray)
                )
            }
        } else {
            val startDate =
                Instant.ofEpochMilli(minTime).atZone(ZoneId.systemDefault()).toLocalDate()
            val endDate = Instant.ofEpochMilli(maxTime).atZone(ZoneId.systemDefault()).toLocalDate()
            val daysBetween = ChronoUnit.DAYS.between(startDate, endDate).toInt()

            for (dayIndex in 0..daysBetween) {
                val currentDate = startDate.plusDays(dayIndex.toLong())
                val millis =
                    currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                val x = pxPadding + ((millis - minTime).toFloat() / xRange.toFloat()) * chartWidth
                val safeX = (x - 20f).coerceAtLeast(0f)
                val labelY = (size.height - pxPadding + 4.dp.toPx()).coerceIn(0f, maxYBound)

                drawText(
                    textMeasurer = textMeasurer,
                    text = currentDate.toString(),
                    topLeft = Offset(safeX, labelY),
                    style = TextStyle(fontSize = 10.sp, color = Color.Gray)
                )

                drawLine(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    start = Offset(x, pxPadding),
                    end = Offset(x, size.height - pxPadding),
                    strokeWidth = 0.5.dp.toPx()
                )
            }
        }

        val path = Path()
        points.forEachIndexed { index, (time, value) ->
            val x = if (range == "Last Day") {
                pxPadding + index.toFloat() * xScale
            } else {
                pxPadding + ((time - minTime).toFloat() / xRange.toFloat()) * chartWidth
            }
            val y = size.height - pxPadding - (value - minValue) * yScale

            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path, color = lineColor, style = Stroke(width = 2.dp.toPx()))

        points.forEachIndexed { index, (time, value) ->
            val x = if (range == "Last Day") {
                pxPadding + index.toFloat() * xScale
            } else {
                pxPadding + ((time - minTime).toFloat() / xRange.toFloat()) * chartWidth
            }
            val y = size.height - pxPadding - (value - minValue) * yScale
            val safeY = y.coerceIn(0f, maxYBound)

            drawCircle(color = lineColor, radius = pointRadius.toPx(), center = Offset(x, safeY))
            drawText(
                textMeasurer = textMeasurer,
                text = String.format("%.1f", value),
                topLeft = Offset((x + 6f).coerceAtLeast(0f), safeY - 16f),
                style = TextStyle(fontSize = 10.sp, color = Color.DarkGray)
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun filterByRange(range: String, entries: List<MonitoringEntry>): List<MonitoringEntry> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")

    val now = System.currentTimeMillis()
    val rangeMillis = when (range) {
        "Last Day" -> 1L * 24 * 60 * 60 * 1000
        "Last 7 Days" -> 7L * 24 * 60 * 60 * 1000
        "Last 30 Days" -> 30L * 24 * 60 * 60 * 1000
        "Last 90 Days" -> 90L * 24 * 60 * 60 * 1000
        else -> Long.MAX_VALUE
    }
    return entries.filter { entry ->
        try {
            val dateMillis =
                LocalDateTime.parse(entry.createdAt, formatter).atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli()
            (now - dateMillis) <= rangeMillis
        } catch (_: Exception) {
            false
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun createPointsFromEntries(
    entries: List<MonitoringEntry>, range: String
): List<Pair<Long, Float>> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
    val points = if (range == "Last Day") {
        entries.mapNotNull { entry ->
            entry.value.toFloatOrNull()?.let { value ->
                val timeMillis = try {
                    LocalDateTime.parse(entry.createdAt, formatter).atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli()
                } catch (_: Exception) {
                    null
                }
                timeMillis?.let { it to value }
            }
        }.sortedBy { it.first }
    } else {
        entries.groupBy { entry ->
            try {
                LocalDateTime.parse(entry.createdAt, formatter).toLocalDate()
            } catch (_: Exception) {
                null
            }
        }.filterKeys { it != null }.map { (date, list) ->
            val avg = list.mapNotNull { it.value.toFloatOrNull() }.average().toFloat()
            val millis = date!!.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            millis to avg
        }.sortedBy { it.first }
    }
    return points
}
