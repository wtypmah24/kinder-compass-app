package com.example.school_companion.ui.tab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.data.api.DeletePhotoRequestDto
import com.example.school_companion.data.model.Child
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox
import com.example.school_companion.ui.card.child.ChildPhotoCard
import com.example.school_companion.ui.dialog.photo.AddPhotoDialog
import com.example.school_companion.ui.viewmodel.ChildrenViewModel
import com.example.school_companion.ui.viewmodel.UiState
import com.example.school_companion.ui.viewmodel.onState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PhotosTab(
    child: Child,
    viewModel: ChildrenViewModel = hiltViewModel(),
) {
    val photosState by viewModel.photosState.collectAsStateWithLifecycle()
    LaunchedEffect(child) {
        viewModel.loadChildPhotos(child.id)
    }
    var showAddDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Add new photo")
        }

        photosState.onState(
            onLoading = { LoadingBox() },
            onError = { msg -> ErrorBox(message = msg) },
            onSuccess = {
                val photos = (photosState as UiState.Success).data

                if (photos.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Keine photos für ${child.name}")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(photos) { photo ->
                            ChildPhotoCard(
                                photo = photo,
                                onDelete = {
                                    viewModel.deleteChildPhotos(
                                        DeletePhotoRequestDto(photo.id),
                                        child.id
                                    )
                                }
                            )
                        }
                    }
                }
            }
        )
    }

    if (showAddDialog) {
        AddPhotoDialog(
            onDismiss = { showAddDialog = false },
            onSave = { file, description ->
                viewModel.addChildPhotos(child.id, file, description)
                showAddDialog = false
            }
        )
    }
}
