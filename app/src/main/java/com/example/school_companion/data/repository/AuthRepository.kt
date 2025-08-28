package com.example.school_companion.data.repository

import com.example.school_companion.config.SessionManager
import com.example.school_companion.data.api.AuthApi
import com.example.school_companion.data.api.LoginRequestDto
import com.example.school_companion.data.api.RegisterRequestDto
import com.example.school_companion.data.model.Companion
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: AuthApi,
    private val sessionManager: SessionManager
) {

    suspend fun login(email: String, password: String): Result<Pair<String, Companion>> {
        return try {
            val response = apiService.login(LoginRequestDto(email, password))
            if (response.isSuccessful) {
                val body = response.body()
                if (!body.isNullOrEmpty()) {
                    val token = body.keys.first()
                    val companion = body[token]!!
                    sessionManager.saveToken(token)
                    Result.success(token to companion)
                } else {
                    Result.failure(Exception("Empty response from server"))
                }
            } else {
                Result.failure(Exception("Login failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        email: String,
        password: String,
        name: String,
        surname: String,
        organization: String,
    ): Result<Unit> {
        return try {
            val response = apiService.register(
                RegisterRequestDto(email, password, name, surname, organization)
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Registration failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            val response = apiService.logout()
            if (response.isSuccessful) {
                sessionManager.clearToken()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Logout failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(): Result<Companion> {
        return try {
            val response = apiService.getUserProfile()
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    Result.success(user)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get profile: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
