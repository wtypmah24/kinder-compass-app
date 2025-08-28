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

    fun loadChildren() {
        viewModelScope.launch {
            _childrenState.value = ChildrenState.Loading
            val result = childrenRepository.getChildren()
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

    fun addChild(child: ChildDto) {
        viewModelScope.launch {
            val result = childrenRepository.addChild(child)
            result.fold(
                onSuccess = {
                    loadChildren()
                },
                onFailure = {
                    loadChildren()
                }
            )
        }
    }

    fun loadChild(childId: Long) {
        viewModelScope.launch {
            val result = childrenRepository.getChild(childId)
            result.fold(
                onSuccess = { child ->
                    _selectedChild.value = child
                },
                onFailure = { exception ->
                    // обработка ошибки
                }
            )
        }
    }

    fun loadChildPhotos(childId: Long) {
        viewModelScope.launch {
            val result = childrenRepository.getChildPhotos(childId)
            result.fold(
                onSuccess = { photos ->
                    _photosState.value = PhotosState.Success(photos)
                },
                onFailure = { exception ->
                    _photosState.value =
                        PhotosState.Error(exception.message ?: "Failed to load photos")
                }
            )
        }
    }

    fun updateChild(childId: Long, child: ChildDto) {
        viewModelScope.launch {
            val result = childrenRepository.updateChild(childId, child)
            result.fold(
                onSuccess = {
                    loadChildren()
                },
                onFailure = {
                    loadChildren()
                }
            )
        }
    }

    fun deleteChild(childId: Long) {
        viewModelScope.launch {
            val result = childrenRepository.deleteChild(childId)
            result.fold(
                onSuccess = {
                    loadChildren()
                },
                onFailure = {
                    loadChildren()
                }
            )
        }
    }

    fun addChildPhotos(childId: Long, file: File, descriptionText: String) {
        viewModelScope.launch {
            val result = childrenRepository.addChildPhotos(childId, file, descriptionText)
            result.fold(
                onSuccess = {
                    loadChildPhotos(childId)
                },
                onFailure = { exception ->
                    _childrenState.value =
                        ChildrenState.Error(exception.message ?: "Failed to add photo")
                    loadChildPhotos(childId)
                }
            )
        }
    }

    fun deleteChildPhotos(deletePhotoRequestDto: DeletePhotoRequestDto, childId: Long) {
        viewModelScope.launch {
            val result = childrenRepository.deleteChildPhoto(deletePhotoRequestDto)
            result.fold(
                onSuccess = {
                    loadChildPhotos(childId)
                },
                onFailure = { exception ->
                    _childrenState.value =
                        ChildrenState.Error(exception.message ?: "Failed to delete photo")
                    loadChildPhotos(childId)
                }
            )
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