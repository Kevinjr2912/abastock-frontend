package com.softgenix.abastock.features.communications.data.repositories

import com.softgenix.abastock.features.communications.data.datasources.remote.api.CommunicationsApi
import com.softgenix.abastock.features.communications.data.datasources.remote.models.RegisterTokenRequestDto
import com.softgenix.abastock.features.communications.domain.repositories.CommunicationsRepository
import javax.inject.Inject

class CommunicationsRepositoryImpl @Inject constructor(
    private val api: CommunicationsApi
) : CommunicationsRepository {

    override suspend fun registerDeviceToken(userId: String, fcmToken: String): Result<Unit> {
        android.util.Log.d("FCM_DEBUG", "API — userId=$userId, token=$fcmToken")
        return try {
            val response = api.registerDeviceToken(RegisterTokenRequestDto(userId, fcmToken))
            android.util.Log.d("FCM_DEBUG", "Respuesta: ${response.code()} — ${response.message()}")
            if (response.isSuccessful) {
                android.util.Log.d("FCM_DEBUG", "Backend confirmó el token")
                Result.success(Unit)
            } else {
                val error = response.errorBody()?.string()
                android.util.Log.e("FCM_DEBUG", "Error del backend: $error")
                Result.failure(Exception("HTTP ${response.code()}: $error"))
            }
        } catch (e: Exception) {
            android.util.Log.e("FCM_DEBUG", "Excepción de red: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun triggerTestSummary(): Result<Unit> {
        return try {
            val response = api.triggerTestSummary()
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("HTTP ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}