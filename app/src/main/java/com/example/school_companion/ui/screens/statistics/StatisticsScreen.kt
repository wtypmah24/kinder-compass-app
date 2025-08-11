package com.example.school_companion.ui.screens.statistics

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.MonitoringEntry
import com.example.school_companion.data.model.MonitoringParam
import com.example.school_companion.data.model.ScaleType
import com.example.school_companion.ui.screens.monitoring.DropdownMenuWrapper
import com.example.school_companion.ui.viewmodel.AuthViewModel
import com.example.school_companion.ui.viewmodel.ChildrenState
import com.example.school_companion.ui.viewmodel.ChildrenViewModel
import com.example.school_companion.ui.viewmodel.EntriesState
import com.example.school_companion.ui.viewmodel.MonitoringEntryViewModel
import com.example.school_companion.ui.viewmodel.MonitoringParamViewModel
import com.example.school_companion.ui.viewmodel.ParamsState
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    paramsViewModel: MonitoringParamViewModel = hiltViewModel(),
    entriesViewModel: MonitoringEntryViewModel = hiltViewModel(),
    childrenViewModel: ChildrenViewModel = hiltViewModel()
) {
    val authToken by authViewModel.authToken.collectAsStateWithLifecycle()
    val paramsState by paramsViewModel.paramsState.collectAsStateWithLifecycle()
    val entriesState by entriesViewModel.entriesState.collectAsStateWithLifecycle()
    val childrenState by childrenViewModel.childrenState.collectAsStateWithLifecycle()

    var selectedChild by remember { mutableStateOf<Child?>(null) }
    var selectedParam by remember { mutableStateOf<MonitoringParam?>(null) }
    var selectedRange by remember { mutableStateOf("Last 7 days") }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val timeRanges = listOf("Last Day", "Last 7 Days", "Last 30 Days", "Last 90 Days")

    LaunchedEffect(authToken) {
        authToken?.let { token ->
            paramsViewModel.loadMonitoringParamData(token)
            entriesViewModel.loadMonitoringEntryByCompanion(token)
            childrenViewModel.loadChildren(token)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistics", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (childrenState is ChildrenState.Success && paramsState is ParamsState.Success) {
                DropdownMenuWrapper(
                    items = (childrenState as ChildrenState.Success).children,
                    selectedItem = selectedChild,
                    onItemSelected = { selectedChild = it },
                    itemToString = { "${it.name} ${it.surname}" },
                    placeholder = "Choose Child"
                )
                DropdownMenuWrapper(
                    items = (paramsState as ParamsState.Success).paramData,
                    selectedItem = selectedParam,
                    onItemSelected = { selectedParam = it },
                    itemToString = { it.title },
                    placeholder = "Choose Parameter"
                )
                DropdownMenuWrapper(
                    items = timeRanges,
                    selectedItem = selectedRange,
                    onItemSelected = { selectedRange = it },
                    itemToString = { it },
                    placeholder = "Time Range"
                )
            }

            if (entriesState is EntriesState.Success) {
                val entries = (entriesState as EntriesState.Success).entryData
                StatisticsSummaryCard(entries)
            }

            val tabs = listOf("View by Parameter", "View by Child")
            ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }


            when (selectedTabIndex) {
                0 -> {
                    if (selectedParam == null) {
                        Text(
                            "Please select a parameter to see statistics charts",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else if (childrenState !is ChildrenState.Success) {
                        Text(
                            "Something went wrong. Try again later.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        val filteredEntries =
                            (entriesState as EntriesState.Success).entryData.filter { e ->
                                e.parameterId == (selectedParam?.id ?: 0)
                            }
                        ViewByParameter(
                            filteredEntries,
                            (childrenState as ChildrenState.Success).children,
                            selectedRange
                        )
                    }

                }

                1 -> {
                    if (selectedChild == null) {
                        Text(
                            "Please select a child to see statistics charts",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        val filteredEntries =
                            (entriesState as EntriesState.Success).entryData.filter { e ->
                                e.childId == (selectedChild?.id ?: 0)
                            }
                        ViewByChild(filteredEntries)
                    }

                }
            }
        }
    }
}

@Composable
fun StatisticsSummaryCard(entries: List<MonitoringEntry>) {
    val totalEntries = entries.size
    val activeChildren = entries.map { it.childId }.distinct().count()
    val parametersTracked = entries.map { it.parameterId }.distinct().count()
    val avgPerDay = if (entries.isNotEmpty()) totalEntries / 30 else 0 // Пример

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Statistics Summary", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Total Entries: $totalEntries")
            Text("Active Children: $activeChildren")
            Text("Parameters Tracked: $parametersTracked")
            Text("Average per Day: $avgPerDay")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewByParameter(entries: List<MonitoringEntry>, children: List<Child>, range: String) {
    if (entries.isEmpty()) {
        Text("No data available")
        return
    }

    val entriesByChild = entries.groupBy { it.childId }

    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        entriesByChild.forEach { (childId, childEntries) ->
            val child = children.find { it.id == childId }
            val childName = "${child?.name ?: "Unknown"} ${child?.surname ?: ""}".trim()

            Text(
                text = "Chart parameter ${childEntries.first().type} for $childName",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            when (childEntries.first().type) {
                ScaleType.SCALE, ScaleType.QUANTITATIVE -> {
                    LineChart(childEntries, range)
                }

                ScaleType.BINARY -> {
                    BinaryBarChart(childEntries)
                }

                ScaleType.QUALITATIVE -> {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        childEntries.forEach {
                            Text("${it.createdAt}: ${it.value}")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ViewByChild(entries: List<MonitoringEntry>) {
    Text("View by Child - Coming soon")
}

@Composable
fun BinaryBarChart(entries: List<MonitoringEntry>) {
    val yesCount = entries.count { it.value == "1" || it.value.equals("yes", true) }
    val noCount = entries.size - yesCount
    Text("Yes: $yesCount, No: $noCount")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LineChart(entries: List<MonitoringEntry>, range: String) {
    if (entries.isEmpty()) {
        Log.d("LineChart", "No entries passed to chart")
        Text("No data")
        return
    }

    val lineColor = MaterialTheme.colorScheme.primary
    val textMeasurer = rememberTextMeasurer()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
    val timeFormatterForLastDay = DateTimeFormatter.ofPattern("HH:mm") // для Last Day по времени

    // -------------------
    // Фильтрация по range
    // -------------------
    val now = System.currentTimeMillis()
    val rangeMillis = when (range) {
        "Last Day" -> 1L * 24 * 60 * 60 * 1000
        "Last 7 Days" -> 7L * 24 * 60 * 60 * 1000
        "Last 30 Days" -> 30L * 24 * 60 * 60 * 1000
        "Last 90 Days" -> 90L * 24 * 60 * 60 * 1000
        else -> Long.MAX_VALUE
    }
    val filtered = entries.filter { entry ->
        try {
            val dateMillis = LocalDateTime.parse(entry.createdAt, formatter)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
            (now - dateMillis) <= rangeMillis
        } catch (_: Exception) {
            false
        }
    }

    Log.d("LineChart", "Filtered entries count=${filtered.size} for range=$range")

    if (filtered.isEmpty()) {
        Text("No data for selected range")
        return
    }

    // -------------------
    // Группировка и усреднение или просто список (Last Day)
    // -------------------
    val points = if (range == "Last Day") {
        filtered.mapNotNull { entry ->
            entry.value.toFloatOrNull()?.let { value ->
                val timeMillis = try {
                    LocalDateTime.parse(entry.createdAt, formatter)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                } catch (_: Exception) {
                    null
                }
                timeMillis?.let { it to value }
            }
        }.sortedBy { it.first }
    } else {
        filtered.groupBy { entry ->
            try {
                LocalDateTime.parse(entry.createdAt, formatter).toLocalDate()
            } catch (_: Exception) {
                null
            }
        }.filterKeys { it != null }
            .map { (date, list) ->
                val avg = list.mapNotNull { it.value.toFloatOrNull() }.average().toFloat()
                val millis = date!!.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                millis to avg
            }
            .sortedBy { it.first }
    }

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

        val xScale =
            chartWidth / (if (range == "Last Day") (points.size - 1).coerceAtLeast(1)
                .toFloat() else xRange.toFloat())
        val yScale = chartHeight / yRange

        // Оси
        drawLine(
            color = Color.Gray,
            start = Offset(pxPadding, size.height - pxPadding),
            end = Offset(size.width - pxPadding, size.height - pxPadding),
            strokeWidth = 1.dp.toPx()
        )
        drawLine(
            color = Color.Gray,
            start = Offset(pxPadding, pxPadding),
            end = Offset(pxPadding, size.height - pxPadding),
            strokeWidth = 1.dp.toPx()
        )

        // Подписи Y
        val yLabelCount = 5
        val yStep = yRange / yLabelCount
        for (i in 0..yLabelCount) {
            val value = minValue + i * yStep
            val y = size.height - pxPadding - (value - minValue) * yScale
            drawText(
                textMeasurer = textMeasurer,
                text = String.format("%.1f", value),
                topLeft = Offset(4f, y - 8f),
                style = TextStyle(fontSize = 10.sp, color = Color.Gray)
            )
            drawLine(
                color = Color.LightGray.copy(alpha = 0.3f),
                start = Offset(pxPadding, y),
                end = Offset(size.width - pxPadding, y),
                strokeWidth = 0.5.dp.toPx()
            )
        }

        // Подписи X
        if (range == "Last Day") {
            // Временная ось равномерно разбита по количеству точек
            points.forEachIndexed { index, (time, _) ->
                val x = pxPadding + index.toFloat() * xScale
                val label = try {
                    val dt =
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
                    dt.format(timeFormatterForLastDay)
                } catch (_: Exception) {
                    ""
                }
                drawText(
                    textMeasurer = textMeasurer,
                    text = label,
                    topLeft = Offset(x - 10f, size.height - pxPadding + 4.dp.toPx()),
                    style = TextStyle(fontSize = 10.sp, color = Color.Gray)
                )
            }
        } else {
            // Для остальных диапазонов — шаг по дням (X ось по дням)
            // Посчитаем количество дней между minTime и maxTime
            val startDate =
                Instant.ofEpochMilli(minTime).atZone(ZoneId.systemDefault()).toLocalDate()
            val endDate = Instant.ofEpochMilli(maxTime).atZone(ZoneId.systemDefault()).toLocalDate()
            val daysBetween = ChronoUnit.DAYS.between(startDate, endDate).toInt()

            // Показываем метки по оси X с шагом 1 день
            for (dayIndex in 0..daysBetween) {
                val currentDate = startDate.plusDays(dayIndex.toLong())
                val millis =
                    currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                val x = pxPadding + ((millis - minTime).toFloat() / xRange.toFloat()) * chartWidth
                val label = currentDate.toString() // или можно форматировать по-другому
                drawText(
                    textMeasurer = textMeasurer,
                    text = label,
                    topLeft = Offset(x - 20f, size.height - pxPadding + 4.dp.toPx()),
                    style = TextStyle(fontSize = 10.sp, color = Color.Gray)
                )
                // Можно добавить тонкие вертикальные линии для удобства чтения графика
                drawLine(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    start = Offset(x, pxPadding),
                    end = Offset(x, size.height - pxPadding),
                    strokeWidth = 0.5.dp.toPx()
                )
            }
        }

        // Линия графика
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

        // Точки и значения
        points.forEachIndexed { index, (time, value) ->
            val x = if (range == "Last Day") {
                pxPadding + index.toFloat() * xScale
            } else {
                pxPadding + ((time - minTime).toFloat() / xRange.toFloat()) * chartWidth
            }
            val y = size.height - pxPadding - (value - minValue) * yScale

            drawCircle(color = lineColor, radius = pointRadius.toPx(), center = Offset(x, y))
            drawText(
                textMeasurer = textMeasurer,
                text = String.format("%.1f", value),
                topLeft = Offset(x + 6f, y - 16f),
                style = TextStyle(fontSize = 10.sp, color = Color.DarkGray)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun FilterByRange(range: String, entries: List<MonitoringEntry>) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")

    val now = System.currentTimeMillis()
    val rangeMillis = when (range) {
        "Last Day" -> 1L * 24 * 60 * 60 * 1000
        "Last 7 Days" -> 7L * 24 * 60 * 60 * 1000
        "Last 30 Days" -> 30L * 24 * 60 * 60 * 1000
        "Last 90 Days" -> 90L * 24 * 60 * 60 * 1000
        else -> Long.MAX_VALUE
    }
    val filtered = entries.filter { entry ->
        try {
            val dateMillis = LocalDateTime.parse(entry.createdAt, formatter)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
            (now - dateMillis) <= rangeMillis
        } catch (_: Exception) {
            false
        }
    }
}
