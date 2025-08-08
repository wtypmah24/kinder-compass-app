package com.example.school_companion.data.api

import com.example.school_companion.data.model.Companion
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login-app")
    suspend fun login(@Body loginRequest: LoginRequestDto): Response<Map<String, Companion>>

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequestDto): Response<AuthResponseDto>

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>

    @GET("auth/me")
    suspend fun getUserProfile(@Header("Authorization") token: String): Response<Companion>
}

data class RegisterRequestDto(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
    val organization: String,
)

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class AuthResponseDto(
    val token: String,
    val companion: Companion
)