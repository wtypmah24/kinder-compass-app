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

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentCompanion = MutableStateFlow<Companion?>(null)
    val currentCompanion: StateFlow<Companion?> = _currentCompanion.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = authRepository.login(email, password)
            result.fold(
                onSuccess = { (token, user) ->
                    sessionManager.saveToken(token)
                    _currentCompanion.value = user
                    _authState.value = AuthState.Success
                },
                onFailure = { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Login failed")
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
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = authRepository.register(email, password, firstName, lastName, role)
            result.fold(
                onSuccess = {
                    _authState.value = AuthState.RegistrationSuccess
                },
                onFailure = { exception ->
                    _authState.value =
                        AuthState.Error(exception.message ?: "Registration failed")
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            val token = sessionManager.getToken()
            if (token != null) {
                val result = authRepository.logout()
                result.fold(
                    onSuccess = {
                        sessionManager.clearToken()
                        _currentCompanion.value = null
                        _authState.value = AuthState.Success
                    },
                    onFailure = { exception ->
                        _authState.value = AuthState.Error(exception.message ?: "Logout failed")
                    }
                )
            } else {
                _authState.value = AuthState.Error("No token to logout")
            }
        }
    }

    fun getUserProfile() {
        viewModelScope.launch {
            val token = sessionManager.getToken()
            if (token != null) {
                val result = authRepository.getUserProfile()
                result.fold(
                    onSuccess = { user ->
                        _currentCompanion.value = user
                    },
                    onFailure = { exception ->
                        _authState.value =
                            AuthState.Error(exception.message ?: "Failed to get profile")
                    }
                )
            } else {
                _authState.value = AuthState.Error("No token available")
            }
        }
    }
}

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data object Success : AuthState()
    data object RegistrationSuccess : AuthState()
    data class Error(val message: String) : AuthState()
}
