package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.config.SessionManager
import com.example.school_companion.data.model.Companion
import com.example.school_companion.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _authState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val authState: StateFlow<UiState<Unit>> = _authState.asStateFlow()

    private val _currentCompanion = MutableStateFlow<UiState<Companion>>(UiState.Idle)
    val currentCompanion: StateFlow<UiState<Companion>> = _currentCompanion.asStateFlow()

    fun login(email: String, password: String) {
        _authState.value = UiState.Loading
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            result.fold(
                onSuccess = { (token, user) ->
                    sessionManager.saveToken(token)
                    _currentCompanion.value = UiState.Success(user)
                    _authState.value = UiState.Success(Unit)
                },
                onFailure = { exception ->
                    val msg = exception.message ?: "Login failed"
                    _authState.value = UiState.Error(msg)
                }
            )
        }
    }

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        role: String
    ) {
        _authState.value = UiState.Loading
        viewModelScope.launch {
            val result = authRepository.register(email, password, firstName, lastName, role)
            handleResult(result, _authState) {
                login(email, password)
            }
        }
    }

    fun logout() {
        _authState.value = UiState.Loading
        viewModelScope.launch {
            val token = sessionManager.getToken()
            if (token != null) {
                val result = authRepository.logout()
                handleResult(result, _authState) {
                    sessionManager.clearToken()
                    _currentCompanion.value = UiState.Idle
                }
            } else {
                _authState.value = UiState.Error("No token to logout")
            }
        }
    }

    fun getUserProfile() {
        _currentCompanion.value = UiState.Loading
        viewModelScope.launch {
            val token = sessionManager.getToken()
            if (token != null) {
                val result = authRepository.getUserProfile()
                handleResult(result, _currentCompanion)
            } else {
                _currentCompanion.value = UiState.Error("No token available")
            }
        }
    }
}
