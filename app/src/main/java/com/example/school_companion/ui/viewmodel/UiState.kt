package com.example.school_companion.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow

sealed class UiState<out T> {
    data object Idle : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

fun <T> handleResult(
    result: Result<T>,
    stateFlow: MutableStateFlow<UiState<T>>,
    onSuccess: ((T) -> Unit)? = null
) {
    stateFlow.value = result.fold(
        onSuccess = {
            onSuccess?.invoke(it)
            UiState.Success(it)
        },
        onFailure = { UiState.Error(it.message ?: "Unknown error") }
    )
}

inline fun <T> UiState<T>.onState(
    onIdle: () -> Unit = {},
    onLoading: () -> Unit = {},
    onSuccess: (T) -> Unit,
    onError: (String) -> Unit = {}
) {
    when (this) {
        UiState.Idle -> onIdle()
        UiState.Loading -> onLoading()
        is UiState.Success -> onSuccess(this.data)
        is UiState.Error -> onError(this.message)
    }
}

fun <T> UiState<T>.getOrNull(): T? = when (this) {
    is UiState.Success -> this.data
    else -> null
}
