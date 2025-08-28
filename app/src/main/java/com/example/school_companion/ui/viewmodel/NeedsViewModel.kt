package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.api.NeedRequestDto
import com.example.school_companion.data.model.SpecialNeed
import com.example.school_companion.data.repository.SpecialNeedsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NeedsViewModel @Inject constructor(
    private val needsRepository: SpecialNeedsRepository
) : ViewModel() {

    private val _needsState = MutableStateFlow<NeedsState>(NeedsState.Loading)
    val needsState: StateFlow<NeedsState> = _needsState.asStateFlow()

    fun loadNeeds(childId: Long) {
        viewModelScope.launch {
            _needsState.value = NeedsState.Loading

            val result = needsRepository.getNeeds(childId)

            result.fold(
                onSuccess = { needs ->
                    _needsState.value = NeedsState.Success(needs)
                },
                onFailure = { exception ->
                    _needsState.value =
                        NeedsState.Error(exception.message ?: "Failed to load notes")
                }
            )
        }
    }

    fun createNeed(need: NeedRequestDto, childId: Long) {
        viewModelScope.launch {
            val result = needsRepository.createNeed(need, childId)

            result.fold(
                onSuccess = {
                    loadNeeds(childId)
                },
                onFailure = { exception ->
                    _needsState.value =
                        NeedsState.Error(exception.message ?: "Failed to create special need")
                }
            )
        }
    }

    fun updateNeed(needId: Long, need: NeedRequestDto, childId: Long) {
        viewModelScope.launch {
            val result = needsRepository.updateNeed(needId, need, childId)

            result.fold(
                onSuccess = {
                    loadNeeds(childId)
                },
                onFailure = { exception ->
                    _needsState.value =
                        NeedsState.Error(exception.message ?: "Failed to update special need")
                }
            )
        }
    }

    fun deleteNeed(needId: Long, childId: Long) {
        viewModelScope.launch {
            val result = needsRepository.deleteNeed(needId)

            result.fold(
                onSuccess = {
                    loadNeeds(childId)
                },
                onFailure = { exception ->
                    _needsState.value =
                        NeedsState.Error(exception.message ?: "Failed to delete special need")
                }
            )
        }
    }

}

sealed class NeedsState {
    data object Loading : NeedsState()
    data class Success(val needs: List<SpecialNeed>) : NeedsState()
    data class Error(val message: String) : NeedsState()
} 