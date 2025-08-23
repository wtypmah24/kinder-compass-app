package com.example.school_companion.ui.selector

import androidx.compose.runtime.Composable
import com.example.school_companion.data.model.MonitoringParam
import com.example.school_companion.ui.dropdown.DropdownMenuWrapper

@Composable
fun ParamSelector(
    params: List<MonitoringParam>,
    selectedParam: MonitoringParam?,
    onSelect: (MonitoringParam?) -> Unit
) {
    DropdownMenuWrapper(
        items = params,
        selectedItem = selectedParam,
        onItemSelected = onSelect,
        itemToString = { it.title },
        placeholder = "Choose Parameter"
    )
}