package com.example.school_companion.data.api

import com.example.school_companion.data.model.Goal
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface GoalApi {
    @POST("goal/child/{childId}")
    suspend fun addGoal(
        @Header("Authorization") token: String,
        @Body goalRequestDto: GoalRequestDto,
        @Path("childId") childId: Long
    ): Response<ResponseBody>

    @PATCH("goal/{goalId}/child/{childId}")
    suspend fun updateGoal(
        @Header("Authorization") token: String,
        @Body goalUpdateDto: GoalRequestDto,
        @Path("childId") childId: Long,
        @Path("goalId") goalId: Long
    )

    @DELETE("goal/{goalId}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("goalId") goalId: Long
    )

    @GET("goal/{goalId}")
    suspend fun getGoalById(
        @Header("Authorization") token: String,
        @Path("goalId") goalId: Long
    ): Response<Goal>

    @GET("goal")
    suspend fun getGoalsByCompanion(
        @Header("Authorization") token: String,
    ): Response<List<Goal>>

    @GET("goal/child/{childId}")
    suspend fun getGoalsByChild(
        @Header("Authorization") token: String,
        @Path("childId") childId: Long
    ): Response<List<Goal>>
}

data class GoalRequestDto(
    val description: String
)


