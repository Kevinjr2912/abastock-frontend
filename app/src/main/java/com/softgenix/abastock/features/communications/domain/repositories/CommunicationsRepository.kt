package com.softgenix.abastock.features.communications.domain.repositories

interface CommunicationsRepository {
    suspend fun registerDeviceToken(userId: String, fcmToken: String): Result<Unit>
    suspend fun triggerTestSummary(): Result<Unit>
}