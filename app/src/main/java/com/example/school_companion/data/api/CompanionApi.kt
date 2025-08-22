package com.example.school_companion.data.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.PATCH
import java.time.LocalTime

interface CompanionApi {
    @PATCH("profile")
    suspend fun update(
        @Header("Authorization") token: String,
        @Body dto: CompanionUpdateDto
    ): Response<ResponseBody>

    @DELETE("profile")
    suspend fun delete(
        @Header("Authorization") token: String,
    ): Response<ResponseBody>

    @DELETE("profile/notifications")
    suspend fun getNotificationStatus(
        @Header("Authorization") token: String,
    ): Response<Boolean>

    @PATCH("profile/notifications")
    suspend fun updateNotificationStatus(
        @Header("Authorization") token: String,
        @Body enabled: Boolean
    ): Response<ResponseBody>

    @PATCH("updatePassword/change-password")
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Body dto: PasswordUpdateDto
    ): Response<ResponseBody>
}

data class CompanionUpdateDto(
    val email: String,
    val name: String,
    val surname: String,
    val organization: String?,
    val startWorkingTime: LocalTime?,
    val endWorkingTime: LocalTime?
)

data class PasswordUpdateDto(
    val currentPassword: String,
    val newPassword: String
)
