package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.api.ChildDto
import com.example.school_companion.data.api.DeletePhotoRequestDto
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.Photo
import com.example.school_companion.data.repository.ChildrenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ChildrenViewModel @Inject constructor(
    private val childrenRepository: ChildrenRepository
) : ViewModel() {

    private val _childrenState = MutableStateFlow<ChildrenState>(ChildrenState.Loading)
    val childrenState: StateFlow<ChildrenState> = _childrenState.asStateFlow()

    private val _selectedChild = MutableStateFlow<Child?>(null)
    val selectedChild: StateFlow<Child?> = _selectedChild.asStateFlow()

    private val _photosState = MutableStateFlow<PhotosState>(PhotosState.Loading)
    val photosState: StateFlow<PhotosState> = _photosState.asStateFlow()

    fun loadChildren(token: String) {
        viewModelScope.launch {
            _childrenState.value = ChildrenState.Loading
            childrenRepository.getChildren(token).collect { result ->
                result.fold(
                    onSuccess = { children ->
                        _childrenState.value = ChildrenState.Success(children)
                    },
                    onFailure = { exception ->
                        _childrenState.value =
                            ChildrenState.Error(exception.message ?: "Failed to load children")
                    }
                )
            }
        }
    }

    fun addChild(child: ChildDto, token: String) {
        viewModelScope.launch {
            childrenRepository.addChild(token, child).collect { result ->
                result.fold(
                    onSuccess = {
                        // Refresh events list
                        loadChildren(token)
                    },
                    onFailure = { exception ->
                        loadChildren(token)
                    }
                )
            }
        }
    }

    fun loadChild(token: String, childId: Long) {
        viewModelScope.launch {
            childrenRepository.getChild(token, childId).collect { result ->
                result.fold(
                    onSuccess = { child ->
                        _selectedChild.value = child
                    },
                    onFailure = { exception ->
                        // Handle error
                    }
                )
            }
        }
    }

    fun loadChildPhotos(token: String, childId: Long) {
        viewModelScope.launch {
            childrenRepository.getChildPhotos(token, childId).collect { result ->
                result.fold(
                    onSuccess = { photos ->
                        _photosState.value = PhotosState.Success(photos)
                    },
                    onFailure = { exception ->
                        // Handle error
                    }
                )
            }
        }
    }

    fun updateChild(token: String, childId: Long, child: ChildDto) {
        viewModelScope.launch {
            childrenRepository.updateChild(token, childId, child).collect { result ->
                result.fold(
                    onSuccess = {
                        // Refresh events list
                        loadChildren(token)
                    },
                    onFailure = { exception ->
                        loadChildren(token)
                    }
                )
            }
        }
    }

    fun deleteChild(token: String, childId: Long) {
        viewModelScope.launch {
            childrenRepository.deleteChild(token, childId).collect { result ->
                result.fold(
                    onSuccess = {
                        loadChildren(token)
                    },
                    onFailure = { exception ->
                        loadChildren(token)
                    }
                )
            }
        }
    }

    fun addChildPhotos(
        token: String,
        childId: Long,
        file: File,
        descriptionText: String
    ) {
        viewModelScope.launch {
            childrenRepository.addChildPhotos(token, childId, file, descriptionText)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            loadChildPhotos(token, childId)
                        },
                        onFailure = { exception ->
                            _childrenState.value =
                                ChildrenState.Error(exception.message ?: "Failed to add photo")
                            loadChildPhotos(token, childId)
                        }
                    )
                }
        }
    }

    fun deleteChildPhotos(
        token: String,
        deletePhotoRequestDto: DeletePhotoRequestDto,
        childId: Long
    ) {
        viewModelScope.launch {
            childrenRepository.deleteChildPhoto(token, deletePhotoRequestDto)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            loadChildPhotos(token, childId)
                        },
                        onFailure = { exception ->
                            _childrenState.value =
                                ChildrenState.Error(exception.message ?: "Failed to delete photo")
                            loadChildPhotos(token, childId)
                        }
                    )
                }
        }
    }
}

sealed class ChildrenState {
    data object Loading : ChildrenState()
    data class Success(val children: List<Child>) : ChildrenState()
    data class Error(val message: String) : ChildrenState()
}

sealed class PhotosState {
    data object Loading : PhotosState()
    data class Success(val photos: List<Photo>) : PhotosState()
    data class Error(val message: String) : PhotosState()
}