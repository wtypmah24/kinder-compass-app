package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.api.SessionApi.SessionUpdateDto
import com.example.school_companion.data.model.WorkSession
import com.example.school_companion.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WorkSessionViewModel @Inject constructor(
    private val workSessionRepository: SessionRepository
) : ViewModel() {

    private val _sessionsState = MutableStateFlow<SessionsState>(SessionsState.Loading)
    val sessions: StateFlow<SessionsState> = _sessionsState.asStateFlow()

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val session: StateFlow<SessionState> = _sessionState.asStateFlow()

    fun startWorkSession() {
        viewModelScope.launch {
            val result = workSessionRepository.startSession()
            result.fold(
                onSuccess = { status() },
                onFailure = { exception ->
                    _sessionState.value =
                        SessionState.Error(exception.message ?: "Failed to start session")
                }
            )
        }
    }

    fun endWorkSession() {
        viewModelScope.launch {
            val result = workSessionRepository.endSession()
            result.fold(
                onSuccess = { status() },
                onFailure = { exception ->
                    _sessionState.value =
                        SessionState.Error(exception.message ?: "Failed to end session")
                }
            )
        }
    }

    fun status() {
        viewModelScope.launch {
            val result = workSessionRepository.status()
            result.fold(
                onSuccess = { session ->
                    _sessionState.value = SessionState.Success(session)
                },
                onFailure = { exception ->
                    _sessionState.value =
                        SessionState.Error(exception.message ?: "Failed to load session")
                }
            )
        }
    }

    fun report(startTime: LocalDate, endTime: LocalDate) {
        viewModelScope.launch {
            _sessionsState.value = SessionsState.Loading
            val result = workSessionRepository.reports(startTime, endTime)
            result.fold(
                onSuccess = { s -> _sessionsState.value = SessionsState.Success(s) },
                onFailure = { exception ->
                    _sessionsState.value =
                        SessionsState.Error(exception.message ?: "Failed to load sessions")
                }
            )
        }
    }

    fun update(sessionId: Long, dto: SessionUpdateDto, startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            val result = workSessionRepository.update(sessionId, dto)
            result.fold(
                onSuccess = { report(startDate, endDate) },
                onFailure = { exception ->
                    _sessionsState.value =
                        SessionsState.Error(exception.message ?: "Failed to update session")
                }
            )
        }
    }

    fun delete(sessionId: Long, startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            val result = workSessionRepository.delete(sessionId)
            result.fold(
                onSuccess = { report(startDate, endDate) },
                onFailure = { exception ->
                    _sessionsState.value =
                        SessionsState.Error(exception.message ?: "Failed to delete session")
                }
            )
        }
    }
}

sealed class SessionsState {
    data object Loading : SessionsState()
    data class Success(val sessions: List<WorkSession>) : SessionsState()
    data class Error(val message: String) : SessionsState()
}

sealed class SessionState {
    data object Loading : SessionState()
    data class Success(val session: WorkSession?) : SessionState()
    data class Error(val message: String) : SessionState()
}
