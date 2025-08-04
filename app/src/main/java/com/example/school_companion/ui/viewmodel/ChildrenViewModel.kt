package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.repository.ChildrenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChildrenViewModel @Inject constructor(
    private val childrenRepository: ChildrenRepository
) : ViewModel() {
    
    private val _childrenState = MutableStateFlow<ChildrenState>(ChildrenState.Loading)
    val childrenState: StateFlow<ChildrenState> = _childrenState.asStateFlow()
    
    private val _selectedChild = MutableStateFlow<Child?>(null)
    val selectedChild: StateFlow<Child?> = _selectedChild.asStateFlow()
    
    fun loadChildren(token: String) {
        viewModelScope.launch {
            _childrenState.value = ChildrenState.Loading
            
            childrenRepository.getChildren(token).collect { result ->
                result.fold(
                    onSuccess = { children ->
                        _childrenState.value = ChildrenState.Success(children)
                    },
                    onFailure = { exception ->
                        _childrenState.value = ChildrenState.Error(exception.message ?: "Failed to load children")
                    }
                )
            }
        }
    }
    
    fun loadChild(token: String, childId: String) {
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
    
    fun selectChild(child: Child) {
        _selectedChild.value = child
    }
    
    fun clearSelectedChild() {
        _selectedChild.value = null
    }
}

sealed class ChildrenState {
    object Loading : ChildrenState()
    data class Success(val children: List<Child>) : ChildrenState()
    data class Error(val message: String) : ChildrenState()
} 