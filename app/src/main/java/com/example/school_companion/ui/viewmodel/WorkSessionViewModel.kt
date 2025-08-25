package com.example.school_companion.ui.viewmodel

import android.util.Log
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

    fun startWorkSession(
        token: String,
    ) {
        viewModelScope.launch {
            workSessionRepository.startSession(token)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            status(token)
                        },
                        onFailure = { exception ->

                        }
                    )
                }
        }
    }

    fun endWorkSession(
        token: String,
    ) {
        viewModelScope.launch {
            workSessionRepository.endSession(token)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            status(token)
                        },
                        onFailure = { exception ->

                        }
                    )
                }
        }
    }

    fun status(token: String) {
        viewModelScope.launch {
            Log.d("WorkSessionViewModel", "status() called with token=$token")
            workSessionRepository.status(token).collect { result ->
                result.fold(
                    onSuccess = { session ->
                        Log.d("WorkSessionViewModel", "status success: $session")
                        _sessionState.value = SessionState.Success(session)
                    },
                    onFailure = { exception ->
                        Log.e(
                            "WorkSessionViewModel",
                            "status failed: ${exception.message}",
                            exception
                        )
                        _sessionState.value =
                            SessionState.Error(exception.message ?: "Failed to load session")
                    }
                )
            }
        }
    }


    fun report(
        token: String,
        startTime: LocalDate,
        endTime: LocalDate
    ) {
        viewModelScope.launch {
            _sessionsState.value = SessionsState.Loading
            workSessionRepository.reports(token, startTime, endTime).collect { result ->
                result.fold(
                    onSuccess = { s ->
                        _sessionsState.value = SessionsState.Success(s)
                    },
                    onFailure = { exception ->
                        _sessionsState.value =
                            SessionsState.Error(exception.message ?: "Failed to load children")
                    }
                )
            }
        }
    }

    fun update(
        token: String,
        sessionId: Long,
        dto: SessionUpdateDto,
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        viewModelScope.launch {
            workSessionRepository.update(token, sessionId, dto)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            report(token, startDate, endDate)
                        },
                        onFailure = { exception ->

                        }
                    )
                }
        }
    }

    fun delete(
        token: String,
        sessionId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        viewModelScope.launch {
            workSessionRepository.delete(token, sessionId)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            report(token, startDate, endDate)
                        },
                        onFailure = { exception ->

                        }
                    )
                }
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