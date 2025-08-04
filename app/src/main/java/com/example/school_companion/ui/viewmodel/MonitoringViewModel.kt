package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.model.MonitoringData
import com.example.school_companion.data.repository.MonitoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonitoringViewModel @Inject constructor(
    private val monitoringRepository: MonitoringRepository
) : ViewModel() {
    
    private val _monitoringState = MutableStateFlow<MonitoringState>(MonitoringState.Loading)
    val monitoringState: StateFlow<MonitoringState> = _monitoringState.asStateFlow()
    
    private val _selectedMonitoringData = MutableStateFlow<MonitoringData?>(null)
    val selectedMonitoringData: StateFlow<MonitoringData?> = _selectedMonitoringData.asStateFlow()
    
    fun loadMonitoringData(
        token: String,
        childId: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ) {
        viewModelScope.launch {
            _monitoringState.value = MonitoringState.Loading
            
            monitoringRepository.getMonitoringData(token, childId, startDate, endDate).collect { result ->
                result.fold(
                    onSuccess = { monitoringData ->
                        _monitoringState.value = MonitoringState.Success(monitoringData)
                    },
                    onFailure = { exception ->
                        _monitoringState.value = MonitoringState.Error(exception.message ?: "Failed to load monitoring data")
                    }
                )
            }
        }
    }
    
    fun createMonitoringData(token: String, monitoringData: MonitoringData) {
        viewModelScope.launch {
            monitoringRepository.createMonitoringData(token, monitoringData).collect { result ->
                result.fold(
                    onSuccess = { createdData ->
                        // Refresh monitoring data list
                        loadMonitoringData(token, monitoringData.childId)
                    },
                    onFailure = { exception ->
                        _monitoringState.value = MonitoringState.Error(exception.message ?: "Failed to create monitoring data")
                    }
                )
            }
        }
    }
    
    fun updateMonitoringData(token: String, monitoringId: String, monitoringData: MonitoringData) {
        viewModelScope.launch {
            monitoringRepository.updateMonitoringData(token, monitoringId, monitoringData).collect { result ->
                result.fold(
                    onSuccess = { updatedData ->
                        // Refresh monitoring data list
                        loadMonitoringData(token, monitoringData.childId)
                    },
                    onFailure = { exception ->
                        _monitoringState.value = MonitoringState.Error(exception.message ?: "Failed to update monitoring data")
                    }
                )
            }
        }
    }
    
    fun selectMonitoringData(monitoringData: MonitoringData) {
        _selectedMonitoringData.value = monitoringData
    }
    
    fun clearSelectedMonitoringData() {
        _selectedMonitoringData.value = null
    }
}

sealed class MonitoringState {
    object Loading : MonitoringState()
    data class Success(val monitoringData: List<MonitoringData>) : MonitoringState()
    data class Error(val message: String) : MonitoringState()
} 