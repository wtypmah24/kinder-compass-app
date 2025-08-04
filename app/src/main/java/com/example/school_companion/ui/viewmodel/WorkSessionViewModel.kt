package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.api.StatisticsResponse
import com.example.school_companion.data.model.WorkSession
import com.example.school_companion.data.repository.WorkSessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkSessionViewModel @Inject constructor(
    private val workSessionRepository: WorkSessionRepository
) : ViewModel() {
    
    private val _workSessionsState = MutableStateFlow<WorkSessionsState>(WorkSessionsState.Loading)
    val workSessionsState: StateFlow<WorkSessionsState> = _workSessionsState.asStateFlow()
    
    private val _currentSession = MutableStateFlow<WorkSession?>(null)
    val currentSession: StateFlow<WorkSession?> = _currentSession.asStateFlow()
    
    private val _statisticsState = MutableStateFlow<StatisticsState>(StatisticsState.Loading)
    val statisticsState: StateFlow<StatisticsState> = _statisticsState.asStateFlow()
    
    fun loadWorkSessions(token: String, startDate: String? = null, endDate: String? = null) {
        viewModelScope.launch {
            _workSessionsState.value = WorkSessionsState.Loading
            
            workSessionRepository.getWorkSessions(token, startDate, endDate).collect { result ->
                result.fold(
                    onSuccess = { sessions ->
                        _workSessionsState.value = WorkSessionsState.Success(sessions)
                    },
                    onFailure = { exception ->
                        _workSessionsState.value = WorkSessionsState.Error(exception.message ?: "Failed to load work sessions")
                    }
                )
            }
        }
    }
    
    fun startWorkSession(token: String) {
        viewModelScope.launch {
            workSessionRepository.startWorkSession(token).collect { result ->
                result.fold(
                    onSuccess = { session ->
                        _currentSession.value = session
                    },
                    onFailure = { exception ->
                        _workSessionsState.value = WorkSessionsState.Error(exception.message ?: "Failed to start work session")
                    }
                )
            }
        }
    }
    
    fun endWorkSession(token: String, sessionId: String) {
        viewModelScope.launch {
            workSessionRepository.endWorkSession(token, sessionId).collect { result ->
                result.fold(
                    onSuccess = { session ->
                        _currentSession.value = null
                        // Refresh work sessions list
                        loadWorkSessions(token)
                    },
                    onFailure = { exception ->
                        _workSessionsState.value = WorkSessionsState.Error(exception.message ?: "Failed to end work session")
                    }
                )
            }
        }
    }
    
    fun loadStatistics(
        token: String,
        childId: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ) {
        viewModelScope.launch {
            _statisticsState.value = StatisticsState.Loading
            
            workSessionRepository.getStatistics(token, childId, startDate, endDate).collect { result ->
                result.fold(
                    onSuccess = { statistics ->
                        _statisticsState.value = StatisticsState.Success(statistics)
                    },
                    onFailure = { exception ->
                        _statisticsState.value = StatisticsState.Error(exception.message ?: "Failed to load statistics")
                    }
                )
            }
        }
    }
    
    fun clearCurrentSession() {
        _currentSession.value = null
    }
}

sealed class WorkSessionsState {
    object Loading : WorkSessionsState()
    data class Success(val workSessions: List<WorkSession>) : WorkSessionsState()
    data class Error(val message: String) : WorkSessionsState()
}

sealed class StatisticsState {
    object Loading : StatisticsState()
    data class Success(val statistics: StatisticsResponse) : StatisticsState()
    data class Error(val message: String) : StatisticsState()
} 