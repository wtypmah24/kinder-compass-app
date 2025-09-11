package com.example.school_companion.ui.selector

import androidx.compose.runtime.Composable
import com.example.school_companion.ui.dropdown.DropdownMenuWrapper

@Composable
fun <T> GenericSelector(
    items: List<T>,
    selectedItem: T?,
    onSelect: (T?) -> Unit,
    itemToString: (T) -> String,
    placeholder: String
) {
    DropdownMenuWrapper(
        items = items,
        selectedItem = selectedItem,
        onItemSelected = onSelect,
        itemToString = itemToString,
        placeholder = placeholder
    )
}
