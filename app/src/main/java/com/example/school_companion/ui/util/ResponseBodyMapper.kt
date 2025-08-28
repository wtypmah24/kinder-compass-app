package com.example.school_companion.ui.util

import okhttp3.ResponseBody
import retrofit2.Response

fun <T> Response<T>.toResult(): Result<T> {
    return if (isSuccessful) {
        body()?.let { Result.success(it) }
            ?: Result.failure(Exception("Empty response body"))
    } else {
        Result.failure(Exception("Request failed: ${code()} ${message()}"))
    }
}

fun Response<ResponseBody>.toResultString(defaultMessage: String): Result<String> {
    return if (isSuccessful) {
        try {
            val msg = body()?.string() ?: defaultMessage
            Result.success(msg)
        } catch (e: Exception) {
            Result.failure(e)
        }
    } else {
        Result.failure(Exception("Request failed: ${code()} ${message()}"))
    }
}
