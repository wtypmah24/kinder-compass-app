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

    private val _childrenState = MutableStateFlow<UiState<List<Child>>>(UiState.Idle)
    val childrenState: StateFlow<UiState<List<Child>>> = _childrenState.asStateFlow()

    private val _selectedChild = MutableStateFlow<UiState<Child>>(UiState.Idle)
    val selectedChild: StateFlow<UiState<Child>> = _selectedChild.asStateFlow()

    private val _photosState = MutableStateFlow<UiState<List<Photo>>>(UiState.Idle)
    val photosState: StateFlow<UiState<List<Photo>>> = _photosState.asStateFlow()

    fun loadChildren() {
        viewModelScope.launch {
            _childrenState.value = UiState.Loading
            val result = childrenRepository.getChildren()
            handleResult(result, _childrenState)
        }
    }

    fun addChild(child: ChildDto) {
        viewModelScope.launch {
            val result = childrenRepository.addChild(child)
            handleResult(result, MutableStateFlow(UiState.Idle)) {
                loadChildren()
            }
        }
    }

    fun loadChild(childId: Long) {
        viewModelScope.launch {
            val result = childrenRepository.getChild(childId)
            handleResult(result, _selectedChild)
        }
    }

    fun loadChildPhotos(childId: Long) {
        viewModelScope.launch {
            val result = childrenRepository.getChildPhotos(childId)
            handleResult(result, _photosState)
        }
    }

    fun updateChild(childId: Long, child: ChildDto) {
        viewModelScope.launch {
            val result = childrenRepository.updateChild(childId, child)
            handleResult(result, MutableStateFlow(UiState.Idle)) {
                loadChildren()
            }
        }
    }

    fun deleteChild(childId: Long) {
        viewModelScope.launch {
            val result = childrenRepository.deleteChild(childId)
            handleResult(result, MutableStateFlow(UiState.Idle)) {
                loadChildren()
            }
        }
    }

    fun addChildPhotos(childId: Long, file: File, descriptionText: String) {
        viewModelScope.launch {
            val result = childrenRepository.addChildPhotos(childId, file, descriptionText)
            handleResult(result, MutableStateFlow(UiState.Idle)) {
                loadChildPhotos(childId)
            }
        }
    }

    fun deleteChildPhotos(deletePhotoRequestDto: DeletePhotoRequestDto, childId: Long) {
        viewModelScope.launch {
            val result = childrenRepository.deleteChildPhoto(deletePhotoRequestDto)
            handleResult(result, MutableStateFlow(UiState.Idle)) {
                loadChildPhotos(childId)
            }
        }
    }
}
