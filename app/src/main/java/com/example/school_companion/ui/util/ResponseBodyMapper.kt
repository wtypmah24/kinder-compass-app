package com.example.school_companion.ui.util

import retrofit2.Response

fun <T> Response<T>.toResult(): Result<T> {
    return if (isSuccessful) {
        body()?.let { Result.success(it) }
            ?: Result.failure(Exception("Empty response body"))
    } else {
        Result.failure(Exception("Request failed: ${code()} ${message()}"))
    }
}

fun <T> Response<T>.toResultString(defaultMessage: String): Result<String> {
    return if (isSuccessful) {
        Result.success(defaultMessage)
    } else {
        Result.failure(Exception("Request failed: ${code()} ${message()}"))
    }
}

fun <T> Response<T>.toResultUnit(): Result<Unit> {
    return if (isSuccessful) {
        Result.success(Unit)
    } else {
        Result.failure(Exception("Request failed: ${code()} ${message()}"))
    }
}
