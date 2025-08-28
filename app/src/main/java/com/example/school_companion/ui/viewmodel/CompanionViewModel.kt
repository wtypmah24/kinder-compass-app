package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.api.CompanionUpdateDto
import com.example.school_companion.data.api.PasswordUpdateDto
import com.example.school_companion.data.repository.CompanionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanionViewModel @Inject constructor(
    private val companionRepository: CompanionRepository,
) : ViewModel() {

    private val _notificationsState =
        MutableStateFlow<NotificationsState>(NotificationsState.Loading)
    val notificationsEnabled: StateFlow<NotificationsState> = _notificationsState.asStateFlow()

    fun updateCompanion(dto: CompanionUpdateDto, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val result = companionRepository.updateCompanion(dto)
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { }
            )
        }
    }

    fun deleteCompanion(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val result = companionRepository.deleteCompanion()
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { }
            )
        }
    }

    fun getNotificationStatus() {
        viewModelScope.launch {
            _notificationsState.value = NotificationsState.Loading
            val result = companionRepository.getNotificationStatus()
            result.fold(
                onSuccess = { enabled ->
                    _notificationsState.value = NotificationsState.Success(enabled)
                },
                onFailure = { }
            )
        }
    }

    fun updateNotificationStatus(enabled: Boolean) {
        viewModelScope.launch {
            val result = companionRepository.updateNotificationStatus(enabled)
            result.fold(
                onSuccess = { getNotificationStatus() },
                onFailure = { }
            )
        }
    }

    fun updatePassword(dto: PasswordUpdateDto) {
        viewModelScope.launch {
            val result = companionRepository.updatePassword(dto)
            result.fold(
                onSuccess = { },
                onFailure = { }
            )
        }
    }
}

sealed class NotificationsState {
    data object Loading : NotificationsState()
    data class Success(val notificationsEnabled: Boolean) : NotificationsState()
    data class Error(val message: String) : NotificationsState()
}
