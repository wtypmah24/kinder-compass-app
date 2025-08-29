package com.example.school_companion.data.repository

import com.example.school_companion.config.SessionManager
import com.example.school_companion.data.api.AuthApi
import com.example.school_companion.data.api.LoginRequestDto
import com.example.school_companion.data.api.RegisterRequestDto
import com.example.school_companion.data.model.Companion
import com.example.school_companion.ui.util.toResult
import com.example.school_companion.ui.util.toResultString

import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: AuthApi,
    private val sessionManager: SessionManager
) {
    suspend fun login(email: String, password: String): Result<Pair<String, Companion>> {
        return try {
            apiService.login(LoginRequestDto(email, password)).toResult().mapCatching { body ->
                if (body.isNotEmpty()) {
                    val token = body.keys.first()
                    val companion = body[token]!!
                    sessionManager.saveToken(token)
                    token to companion
                } else {
                    throw Exception("Empty response from server")
                }
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
            apiService.register(
                RegisterRequestDto(email, password, name, surname, organization)
            ).toResultString("Registration successful").map { Unit }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            apiService.logout()
                .toResultString("Logout successful")
                .onSuccess { sessionManager.clearToken() }
                .map { Unit }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(): Result<Companion> {
        return try {
            apiService.getUserProfile().toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
