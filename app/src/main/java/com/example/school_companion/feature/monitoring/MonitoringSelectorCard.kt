package com.example.school_companion.feature.monitoring

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.school_companion.ui.selector.GenericSelector

@Composable
fun MonitoringSelectorCard(
    children: List<Child>,
    selectedChild: Child?,
    onChildSelected: (Child?) -> Unit,
    params: List<MonitoringParam>,
    selectedParam: MonitoringParam?,
    onParamSelected: (MonitoringParam?) -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Select Child", fontWeight = FontWeight.Medium)
                GenericSelector(
                    items = children,
                    selectedItem = selectedChild,
                    onSelect = onChildSelected,
                    itemToString = { "${it.name} ${it.surname}" },
                    placeholder = "Choose Child"
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Select Monitoring Parameter", fontWeight = FontWeight.Medium)
                GenericSelector(
                    items = params,
                    selectedItem = selectedParam,
                    onSelect = onParamSelected,
                    itemToString = { it.title },
                    placeholder = "Choose Parameter"
                )

            }
        }
    }

}