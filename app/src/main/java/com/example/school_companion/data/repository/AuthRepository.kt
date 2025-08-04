package com.example.school_companion.data.repository

import com.example.school_companion.data.api.ApiService
import com.example.school_companion.data.api.LoginRequest
import com.example.school_companion.data.api.RegisterRequest
import com.example.school_companion.data.model.Companion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun login(email: String, password: String): Flow<Result<Pair<String, Companion?>>> = flow {
        try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val token = response.body()?.string()
                if (!token.isNullOrBlank()) {
                    emit(Result.success(Pair(token, null))) // user = null, так как сервер не прислал его
                } else {
                    emit(Result.failure(Exception("Empty token")))
                }
            } else {
                emit(Result.failure(Exception("Login failed: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }


    suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        role: String
    ): Flow<Result<Pair<String, Companion>>> = flow {
        try {
            val response = apiService.register(
                RegisterRequest(email, password, firstName, lastName, role)
            )
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    emit(Result.success(Pair(authResponse.token, authResponse.companion)))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Registration failed: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun logout(token: String): Flow<Result<Unit>> = flow {
        try {
            val response = apiService.logout("Bearer $token")
            if (response.isSuccessful) {
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(Exception("Logout failed: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun getUserProfile(token: String): Flow<Result<Companion>> = flow {
        try {
            val response = apiService.getUserProfile("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    emit(Result.success(user))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to get profile: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun updateUserProfile(token: String, companion: Companion): Flow<Result<Companion>> = flow {
        try {
            val response = apiService.updateUserProfile("Bearer $token", companion)
            if (response.isSuccessful) {
                response.body()?.let { updatedUser ->
                    emit(Result.success(updatedUser))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to update profile: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
object SessionManager {
    var token: String? = null
}