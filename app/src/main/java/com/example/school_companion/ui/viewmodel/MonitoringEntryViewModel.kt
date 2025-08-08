package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                            EntriesState.Error(exception.message ?: "Failed to load monitoring data")
                    }
                )
            }
        }
    }

    //    fun createMonitoringData(token: String, monitoringParam: MonitoringParam) {
//        viewModelScope.launch {
//            monitoringRepository.createMonitoringData(token, monitoringParam).collect { result ->
//                result.fold(
//                    onSuccess = { createdData ->
//                        // Refresh monitoring data list
//                        loadMonitoringData(token, monitoringParam.childId)
//                    },
//                    onFailure = { exception ->
//                        _monitoringState.value = MonitoringState.Error(exception.message ?: "Failed to create monitoring data")
//                    }
//                )
//            }
//        }
//    }
//
//    fun updateMonitoringData(token: String, monitoringId: String, monitoringParam: MonitoringParam) {
//        viewModelScope.launch {
//            monitoringRepository.updateMonitoringData(token, monitoringId, monitoringParam).collect { result ->
//                result.fold(
//                    onSuccess = { updatedData ->
//                        // Refresh monitoring data list
//                        loadMonitoringData(token, monitoringParam.childId)
//                    },
//                    onFailure = { exception ->
//                        _monitoringState.value = MonitoringState.Error(exception.message ?: "Failed to update monitoring data")
//                    }
//                )
//            }
//        }
//    }
//
    fun selectMonitoringData(monitoringParam: MonitoringEntry) {
        _selectedMonitoringEntry.value = monitoringParam
    }

    fun clearSelectedMonitoringData() {
        _selectedMonitoringEntry.value = null
    }
}

sealed class EntriesState {
    object Loading : EntriesState()
    data class Success(val entryData: List<MonitoringEntry>) : EntriesState()
    data class Error(val message: String) : EntriesState()
} 