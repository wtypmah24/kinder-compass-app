package com.example.school_companion.ui.section.children

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.school_companion.ui.card.child.ChildCard
import com.example.school_companion.ui.navigation.Screen
import com.example.school_companion.ui.viewmodel.ChildrenState
import com.example.school_companion.ui.viewmodel.ChildrenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChildrenSection(
    childrenState: ChildrenState,
    childrenViewModel: ChildrenViewModel,
    navController: NavController,
    maxItems: Int = 3,
    onShowAllClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Ihre Kinder",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        when (childrenState) {
            is ChildrenState.Loading -> {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is ChildrenState.Success -> {
                val children = childrenState.children
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
                            childrenViewModel = childrenViewModel,
                            child = child,
                            onClick = {
                                navController.navigate("${Screen.ChildDetail.route}/${child.id}")
                            }
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

            is ChildrenState.Error -> {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = childrenState.message,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
