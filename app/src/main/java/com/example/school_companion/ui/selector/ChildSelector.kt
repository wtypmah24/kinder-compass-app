package com.example.school_companion.ui.selector

import androidx.compose.runtime.Composable
import com.example.school_companion.data.model.Child
import com.example.school_companion.ui.dropdown.DropdownMenuWrapper

@Composable
fun ChildSelector(
    children: List<Child>, selectedChild: Child?, onSelect: (Child) -> Unit
) {
    DropdownMenuWrapper(
        items = children,
        selectedItem = selectedChild,
        onItemSelected = onSelect,
        itemToString = { "${it.name} ${it.surname}" },
        placeholder = "Choose Child"
    )
}