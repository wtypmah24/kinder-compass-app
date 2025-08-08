package com.example.school_companion.ui.viewmodel

import androidx.compose.runtime.sourceInformation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _currentCompanion = MutableStateFlow<Companion?>(null)
    val currentCompanion: StateFlow<Companion?> = _currentCompanion.asStateFlow()
    
    private val _authToken = MutableStateFlow<String?>(null)
    val authToken: StateFlow<String?> = _authToken.asStateFlow()
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            authRepository.login(email, password).collect { result ->
                result.fold(
                    onSuccess = { (token, user) ->
                        println("in AuthViewModel token: $token, user: $user ")
                        _authToken.value = token
                        _currentCompanion.value = user
                        _authState.value = AuthState.Success
                    },
                    onFailure = { exception ->
                        _authState.value = AuthState.Error(exception.message ?: "Login failed")
                    }
                )
            }
        }
    }

    fun register(email: String, password: String, firstName: String, lastName: String, role: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            authRepository.register(email, password, firstName, lastName, role).collect { result ->
                result.fold(
                    onSuccess = {
                        _authState.value = AuthState.RegistrationSuccess
                    },
                    onFailure = { exception ->
                        _authState.value = AuthState.Error(exception.message ?: "Registration failed")
                    }
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _authToken.value?.let { token ->
                authRepository.logout(token).collect { result ->
                    result.fold(
                        onSuccess = {
                            _authToken.value = null
                            _currentCompanion.value = null
                            _authState.value = AuthState.Success
                        },
                        onFailure = { exception ->
                            _authState.value = AuthState.Error(exception.message ?: "Logout failed")
                        }
                    )
                }
            }
        }
    }
    
    fun getUserProfile() {
        viewModelScope.launch {
            _authToken.value?.let { token ->
                authRepository.getUserProfile(token).collect { result ->
                    result.fold(
                        onSuccess = { user ->
                            _currentCompanion.value = user
                        },
                        onFailure = { exception ->
                            _authState.value = AuthState.Error(exception.message ?: "Failed to get profile")
                        }
                    )
                }
            }
        }
    }
    
    fun clearError() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    object RegistrationSuccess : AuthState()
    data class Error(val message: String) : AuthState()
}
