package com.example.school_companion.data.repository

import com.example.school_companion.data.api.AuthApi
import com.example.school_companion.data.api.LoginRequestDto
import com.example.school_companion.data.api.RegisterRequestDto
import com.example.school_companion.data.model.Companion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: AuthApi
) {

    suspend fun login(email: String, password: String): Flow<Result<Pair<String, Companion>>> =
        flow {
            try {
                val response = apiService.login(LoginRequestDto(email, password))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (!body.isNullOrEmpty()) {

                        val token = body.keys.first()
                        val companion = body[token]!!
                        println("token: $token")
                        println("companion: $companion")
                        emit(Result.success(Pair(token, companion)))
                    } else {
                        emit(Result.failure(Exception("Empty response from server")))
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
        name: String,
        surname: String,
        organization: String,
    ): Flow<Result<Unit>> = flow {
        try {
            val response = apiService.register(
                RegisterRequestDto(email, password, name, surname, organization)
            )
            if (response.isSuccessful) {
                emit(Result.success(Unit))
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
}

object SessionManager {
    var token: String? = null
}