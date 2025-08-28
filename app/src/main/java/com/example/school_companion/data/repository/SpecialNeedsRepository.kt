package com.example.school_companion.data.repository

import com.example.school_companion.data.api.NeedRequestDto
import com.example.school_companion.data.api.SpecialNeedApi
import com.example.school_companion.data.model.SpecialNeed
import com.example.school_companion.ui.util.toResult
import com.example.school_companion.ui.util.toResultString
import javax.inject.Inject

class SpecialNeedsRepository @Inject constructor(
    private val apiService: SpecialNeedApi
) {
    suspend fun getNeeds(childId: Long): Result<List<SpecialNeed>> {
        return try {
            apiService.getNeedsByChild(childId).toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createNeed(need: NeedRequestDto, childId: Long): Result<String> {
        return try {
            apiService.addNeed(need, childId)
                .toResultString("Special need added successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateNeed(
        needId: Long,
        need: NeedRequestDto,
        childId: Long
    ): Result<String> {
        return try {
            apiService.updateNeed(need, childId, needId)
                .toResultString("Special need updated successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteNeed(needId: Long): Result<String> {
        return try {
            apiService.delete(needId)
                .toResultString("Special need deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 