package com.example.school_companion.ui.card.statistic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.MonitoringParam
import com.example.school_companion.ui.dropdown.DropdownMenuWrapper

@Composable
fun StatisticsSelectorCard(
    children: List<Child>,
    selectedChild: Child?,
    onChildSelected: (Child?) -> Unit,
    params: List<MonitoringParam>,
    selectedParam: MonitoringParam?,
    onParamSelected: (MonitoringParam?) -> Unit,
    ranges: List<String>,
    selectedRange: String?,
    onRangeSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Select Child", fontWeight = FontWeight.Medium)
            DropdownMenuWrapper(
                items = children,
                selectedItem = selectedChild,
                onItemSelected = onChildSelected,
                itemToString = { "${it.name} ${it.surname}" },
                placeholder = "Choose Child"
            )

            Text("Select Parameter", fontWeight = FontWeight.Medium)
            DropdownMenuWrapper(
                items = params,
                selectedItem = selectedParam,
                onItemSelected = onParamSelected,
                itemToString = { it.title },
                placeholder = "Choose Parameter"
            )

            Text("Select Time Range", fontWeight = FontWeight.Medium)
            DropdownMenuWrapper(
                items = ranges,
                selectedItem = selectedRange,
                onItemSelected = onRangeSelected,
                itemToString = { it },
                placeholder = "Time Range"
            )
        }
    }
}

