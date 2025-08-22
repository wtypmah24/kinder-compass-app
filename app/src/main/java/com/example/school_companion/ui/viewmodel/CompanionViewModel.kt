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

    fun updateCompanion(token: String, dto: CompanionUpdateDto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            companionRepository.updateCompanion(token, dto)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            onSuccess()
                        },
                        onFailure = {

                        }
                    )
                }
        }
    }

    fun deleteCompanion(token: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            companionRepository.deleteCompanion(token)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            onSuccess()
                        },
                        onFailure = {

                        }
                    )
                }
        }
    }

    fun getNotificationStatus(token: String) {
        viewModelScope.launch {
            _notificationsState.value = NotificationsState.Loading
            companionRepository.getNotificationStatus(token)
                .collect { result ->
                    result.fold(
                        onSuccess = { e ->
                            _notificationsState.value = NotificationsState.Success(e)
                        },
                        onFailure = {

                        }
                    )
                }
        }
    }

    fun updateNotificationStatus(token: String, enabled: Boolean) {
        viewModelScope.launch {
            companionRepository.updateNotificationStatus(token, enabled)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            getNotificationStatus(token)
                        },
                        onFailure = {

                        }
                    )
                }
        }
    }

    fun updatePassword(token: String, dto: PasswordUpdateDto) {
        viewModelScope.launch {
            companionRepository.updatePassword(token, dto)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                        },
                        onFailure = {

                        }
                    )
                }
        }
    }

}

sealed class NotificationsState {
    data object Loading : NotificationsState()
    data class Success(val notificationsEnabled: Boolean) : NotificationsState()
    data class Error(val message: String) : NotificationsState()
}
