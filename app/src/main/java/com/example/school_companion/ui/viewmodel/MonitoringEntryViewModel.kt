package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.api.EntryRequestDto
import com.example.school_companion.data.model.MonitoringEntry
import com.example.school_companion.data.repository.MonitoringEntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonitoringEntryViewModel @Inject constructor(
    private val monitoringEntryRepository: MonitoringEntryRepository
) : ViewModel() {

    private val _entriesState = MutableStateFlow<EntriesState>(EntriesState.Loading)
    val entriesState: StateFlow<EntriesState> = _entriesState.asStateFlow()

    fun loadMonitoringEntryData(childId: Long) {
        viewModelScope.launch {
            _entriesState.value = EntriesState.Loading
            val result = monitoringEntryRepository.getMonitoringEntryData(childId)
            result.fold(
                onSuccess = { monitoringData ->
                    _entriesState.value = EntriesState.Success(monitoringData)
                },
                onFailure = { exception ->
                    _entriesState.value =
                        EntriesState.Error(exception.message ?: "Failed to load monitoring data")
                }
            )
        }
    }

    fun loadMonitoringEntryByCompanion() {
        viewModelScope.launch {
            _entriesState.value = EntriesState.Loading
            val result = monitoringEntryRepository.getMonitoringEntryByCompanion()
            result.fold(
                onSuccess = { monitoringData ->
                    _entriesState.value = EntriesState.Success(monitoringData)
                },
                onFailure = { exception ->
                    _entriesState.value =
                        EntriesState.Error(exception.message ?: "Failed to load monitoring data")
                }
            )
        }
    }

    fun createMonitoringEntry(entry: EntryRequestDto, childId: Long, paramId: Long) {
        viewModelScope.launch {
            val result = monitoringEntryRepository.createMonitoringEntry(entry, childId, paramId)
            result.fold(
                onSuccess = {
                    loadMonitoringEntryData(childId)
                },
                onFailure = { exception ->
                    _entriesState.value =
                        EntriesState.Error(exception.message ?: "Failed to create monitoring data")
                }
            )
        }
    }

    fun updateMonitoringEntry(entry: EntryRequestDto, childId: Long, paramId: Long) {
        viewModelScope.launch {
            val result = monitoringEntryRepository.updateMonitoringEntry(entry, childId, paramId)
            result.fold(
                onSuccess = {
                    loadMonitoringEntryData(childId)
                },
                onFailure = { exception ->
                    _entriesState.value =
                        EntriesState.Error(exception.message ?: "Failed to update monitoring data")
                }
            )
        }
    }

    fun deleteMonitoringEntry(entryId: Long, childId: Long) {
        viewModelScope.launch {
            val result = monitoringEntryRepository.deleteMonitoringEntry(entryId, childId)
            result.fold(
                onSuccess = {
                    loadMonitoringEntryData(childId)
                },
                onFailure = { exception ->
                    _entriesState.value =
                        EntriesState.Error(exception.message ?: "Failed to delete monitoring data")
                }
            )
        }
    }
}

sealed class EntriesState {
    data object Loading : EntriesState()
    data class Success(val entryData: List<MonitoringEntry>) : EntriesState()
    data class Error(val message: String) : EntriesState()
}
