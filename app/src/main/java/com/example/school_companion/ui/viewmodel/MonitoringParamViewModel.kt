package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.api.ParamRequestDto
import com.example.school_companion.data.model.MonitoringParam
import com.example.school_companion.data.repository.MonitoringParamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonitoringParamViewModel @Inject constructor(
    private val monitoringParamRepository: MonitoringParamRepository
) : ViewModel() {

    private val _paramsState = MutableStateFlow<ParamsState>(ParamsState.Loading)
    val paramsState: StateFlow<ParamsState> = _paramsState.asStateFlow()

    private val _selectedMonitoringParam = MutableStateFlow<MonitoringParam?>(null)
    val selectedMonitoringParam: StateFlow<MonitoringParam?> =
        _selectedMonitoringParam.asStateFlow()

    fun loadMonitoringParamData(
        token: String,
    ) {
        viewModelScope.launch {
            _paramsState.value = ParamsState.Loading

            monitoringParamRepository.getMonitoringParamData(token).collect { result ->
                result.fold(
                    onSuccess = { monitoringData ->
                        _paramsState.value = ParamsState.Success(monitoringData)
                    },
                    onFailure = { exception ->
                        _paramsState.value =
                            ParamsState.Error(
                                exception.message ?: "Failed to load monitoring params data"
                            )
                    }
                )
            }
        }
    }

    fun createMonitoringParam(
        token: String,
        param: ParamRequestDto,
    ) {
        viewModelScope.launch {
            monitoringParamRepository.createMonitoringParam(token, param)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            loadMonitoringParamData(token)
                        },
                        onFailure = { exception ->
                            _paramsState.value = ParamsState.Error(
                                exception.message ?: "Failed to create monitoring param data"
                            )
                        }
                    )
                }
        }
    }

    fun updateMonitoringParam(
        token: String,
        param: ParamRequestDto,
        paramId: Long
    ) {
        viewModelScope.launch {
            monitoringParamRepository.updateMonitoringParam(token, param, paramId)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            loadMonitoringParamData(token)
                        },
                        onFailure = { exception ->
                            _paramsState.value = ParamsState.Error(
                                exception.message ?: "Failed to update monitoring param data"
                            )
                        }
                    )
                }
        }
    }

    fun deleteMonitoringParam(
        token: String,
        paramId: Long,
    ) {
        viewModelScope.launch {
            monitoringParamRepository.deleteMonitoringParam(token, paramId)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            loadMonitoringParamData(token)
                        },
                        onFailure = { exception ->
                            _paramsState.value = ParamsState.Error(
                                exception.message ?: "Failed to delete monitoring param data"
                            )
                        }
                    )
                }
        }
    }
}

sealed class ParamsState {
    data object Loading : ParamsState()
    data class Success(val paramData: List<MonitoringParam>) : ParamsState()
    data class Error(val message: String) : ParamsState()
} 