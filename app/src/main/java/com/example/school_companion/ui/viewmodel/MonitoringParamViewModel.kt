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

    fun loadMonitoringParamData() {
        viewModelScope.launch {
            _paramsState.value = ParamsState.Loading
            val result = monitoringParamRepository.getMonitoringParamData()
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

    fun createMonitoringParam(param: ParamRequestDto) {
        viewModelScope.launch {
            val result = monitoringParamRepository.createMonitoringParam(param)
            result.fold(
                onSuccess = {
                    loadMonitoringParamData()
                },
                onFailure = { exception ->
                    _paramsState.value =
                        ParamsState.Error(
                            exception.message ?: "Failed to create monitoring param data"
                        )
                }
            )
        }
    }

    fun updateMonitoringParam(param: ParamRequestDto, paramId: Long) {
        viewModelScope.launch {
            val result = monitoringParamRepository.updateMonitoringParam(param, paramId)
            result.fold(
                onSuccess = {
                    loadMonitoringParamData()
                },
                onFailure = { exception ->
                    _paramsState.value =
                        ParamsState.Error(
                            exception.message ?: "Failed to update monitoring param data"
                        )
                }
            )
        }
    }

    fun deleteMonitoringParam(paramId: Long) {
        viewModelScope.launch {
            val result = monitoringParamRepository.deleteMonitoringParam(paramId)
            result.fold(
                onSuccess = {
                    loadMonitoringParamData()
                },
                onFailure = { exception ->
                    _paramsState.value =
                        ParamsState.Error(
                            exception.message ?: "Failed to delete monitoring param data"
                        )
                }
            )
        }
    }
}

sealed class ParamsState {
    data object Loading : ParamsState()
    data class Success(val paramData: List<MonitoringParam>) : ParamsState()
    data class Error(val message: String) : ParamsState()
}
