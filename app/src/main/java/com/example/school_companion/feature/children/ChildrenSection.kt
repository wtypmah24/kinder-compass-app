package com.example.school_companion.feature.children

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.school_companion.data.model.Child
import com.example.school_companion.feature.children.child.ChildAction
import com.example.school_companion.feature.children.child.ChildCard
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox
import com.example.school_companion.ui.util.UiState
import com.example.school_companion.ui.util.onState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChildrenSection(
    childrenState: UiState<List<Child>>,
    maxItems: Int = 3,
    onChildAction: (Child, ChildAction) -> Unit,
    onShowAllClick: (() -> Unit)? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Ihre Kinder",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        childrenState.onState(
            onLoading = { LoadingBox() },
            onError = { msg -> ErrorBox(message = msg) },
            onSuccess = { children ->
                if (children.isEmpty()) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.People,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Keine Kinder zugewiesen",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                } else {
                    children.take(maxItems).forEach { child ->
                        ChildCard(
                            child = child,
                            onAction = onChildAction
                        )
                    }

                    if (onShowAllClick != null && children.size > maxItems) {
                        TextButton(
                            onClick = onShowAllClick,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Alle ${children.size} Kinder anzeigen")
                        }
                    }
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Children Section - Empty")
@Composable
fun ChildrenSectionPreview_Empty() {
    ChildrenSection(
        childrenState = UiState.Success(emptyList()),
        onChildAction = { _, _ -> }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Children Section - With Children")
@Composable
fun ChildrenSectionPreview_WithChildren() {
    val sampleChildren = listOf(
        Child(
            id = 1L,
            name = "Alice",
            surname = "Johnson",
            email = "alice@example.com",
            phoneNumber = "+123456789",
            dateOfBirth = "2015-06-21",
            active = true
        ),
        Child(
            id = 2L,
            name = "Bob",
            surname = "Smith",
            email = "bob@example.com",
            phoneNumber = "+987654321",
            dateOfBirth = "2013-09-12",
            active = true
        )
    )

    ChildrenSection(
        childrenState = UiState.Success(sampleChildren),
        maxItems = 3,
        onChildAction = { _, _ -> }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Children Section - Show All Button")
@Composable
fun ChildrenSectionPreview_ShowAll() {
    val sampleChildren = listOf(
        Child(
            id = 1L,
            name = "Alice",
            surname = "Johnson",
            email = "",
            phoneNumber = "",
            dateOfBirth = "",
            active = true
        ),
        Child(
            id = 2L,
            name = "Bob",
            surname = "Smith",
            email = "",
            phoneNumber = "",
            dateOfBirth = "",
            active = true
        ),
        Child(
            id = 3L,
            name = "Charlie",
            surname = "Brown",
            email = "",
            phoneNumber = "",
            dateOfBirth = "",
            active = true
        ),
        Child(
            id = 4L,
            name = "Diana",
            surname = "Prince",
            email = "",
            phoneNumber = "",
            dateOfBirth = "",
            active = true
        )
    )

    ChildrenSection(
        childrenState = UiState.Success(sampleChildren),
        maxItems = 3,
        onChildAction = { _, _ -> },
        onShowAllClick = {}
    )
}
