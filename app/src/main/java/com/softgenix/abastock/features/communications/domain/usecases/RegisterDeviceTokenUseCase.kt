package com.softgenix.abastock.features.communications.domain.usecases

import com.softgenix.abastock.core.data.local.TokenManager
import com.softgenix.abastock.features.communications.domain.repositories.CommunicationsRepository
import javax.inject.Inject

class RegisterDeviceTokenUseCase @Inject constructor(
    private val repository: CommunicationsRepository,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(fcmToken: String): Result<Unit> {
        val session = tokenManager.getSession()
        android.util.Log.d("FCM_DEBUG", "UseCase — session=$session, token=$fcmToken")

        val userId = session?.userId
            ?: run {
                android.util.Log.e("FCM_DEBUG", "UseCase: userId es null — sesión no encontrada")
                return Result.failure(Exception("Sin sesión activa"))
            }

        return repository.registerDeviceToken(userId, fcmToken)
    }
}