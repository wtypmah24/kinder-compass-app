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

    private val _selectedMonitoringEntry = MutableStateFlow<MonitoringEntry?>(null)
    val selectedMonitoringEntry: StateFlow<MonitoringEntry?> =
        _selectedMonitoringEntry.asStateFlow()

    fun loadMonitoringEntryData(
        token: String,
        childId: Long
    ) {
        viewModelScope.launch {
            _entriesState.value = EntriesState.Loading

            monitoringEntryRepository.getMonitoringEntryData(token, childId).collect { result ->
                result.fold(
                    onSuccess = { monitoringData ->
                        _entriesState.value = EntriesState.Success(monitoringData)
                    },
                    onFailure = { exception ->
                        _entriesState.value =
                            EntriesState.Error(
                                exception.message ?: "Failed to load monitoring data"
                            )
                    }
                )
            }
        }
    }

    fun loadMonitoringEntryByCompanion(
        token: String
    ) {
        viewModelScope.launch {
            _entriesState.value = EntriesState.Loading

            monitoringEntryRepository.getMonitoringEntryByCompanion(token).collect { result ->
                result.fold(
                    onSuccess = { monitoringData ->
                        _entriesState.value = EntriesState.Success(monitoringData)
                    },
                    onFailure = { exception ->
                        _entriesState.value =
                            EntriesState.Error(
                                exception.message ?: "Failed to load monitoring data"
                            )
                    }
                )
            }
        }
    }

    fun createMonitoringEntry(
        token: String,
        entry: EntryRequestDto,
        childId: Long,
        paramId: Long
    ) {
        viewModelScope.launch {
            monitoringEntryRepository.createMonitoringEntry(token, entry, childId, paramId)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            loadMonitoringEntryData(token, childId)
                        },
                        onFailure = { exception ->
                            _entriesState.value = EntriesState.Error(
                                exception.message ?: "Failed to create monitoring data"
                            )
                        }
                    )
                }
        }
    }

    fun updateMonitoringEntry(
        token: String,
        entry: EntryRequestDto,
        childId: Long,
        paramId: Long
    ) {
        viewModelScope.launch {
            monitoringEntryRepository.updateMonitoringEntry(token, entry, childId, paramId)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            loadMonitoringEntryData(token, childId)
                        },
                        onFailure = { exception ->
                            _entriesState.value = EntriesState.Error(
                                exception.message ?: "Failed to update monitoring data"
                            )
                        }
                    )
                }
        }
    }

    fun deleteMonitoringEntry(
        token: String,
        entryId: Long,
        childId: Long,
    ) {
        viewModelScope.launch {
            monitoringEntryRepository.deleteMonitoringEntry(token, entryId, childId)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            loadMonitoringEntryData(token, childId)
                        },
                        onFailure = { exception ->
                            _entriesState.value = EntriesState.Error(
                                exception.message ?: "Failed to delete monitoring data"
                            )
                        }
                    )
                }
        }
    }

    fun selectMonitoringData(monitoringParam: MonitoringEntry) {
        _selectedMonitoringEntry.value = monitoringParam
    }

    fun clearSelectedMonitoringData() {
        _selectedMonitoringEntry.value = null
    }
}

sealed class EntriesState {
    data object Loading : EntriesState()
    data class Success(val entryData: List<MonitoringEntry>) : EntriesState()
    data class Error(val message: String) : EntriesState()
} 