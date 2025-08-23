package com.example.school_companion.ui.dropdown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.ContentAlpha

@Composable
fun <T> DropdownMenuWrapper(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T?) -> Unit,
    itemToString: (T) -> String,
    placeholder: String = "Select",
    extraItem: T? = null,
    extraItemText: String = "All",
    customItemContent: (@Composable (T?) -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = selectedItem?.let { itemToString(it) }
        ?: if (selectedItem == null && extraItem != null) extraItemText else placeholder

    Box {
        OutlinedTextField(
            value = displayText,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            },
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
                disabledBorderColor = MaterialTheme.colorScheme.primary,
                disabledTrailingIconColor = MaterialTheme.colorScheme.primary,
                disabledLabelColor = MaterialTheme.colorScheme.primary
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (extraItem != null) {
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(null)
                        expanded = false
                    },
                    text = {
                        if (customItemContent != null) customItemContent(null)
                        else Text(extraItemText)
                    }
                )
            }

            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    },
                    text = {
                        if (customItemContent != null) customItemContent(item)
                        else Text(itemToString(item))
                    }
                )
            }
        }
    }
}
