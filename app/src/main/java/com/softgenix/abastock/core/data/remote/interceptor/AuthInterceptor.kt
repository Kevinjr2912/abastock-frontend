package com.softgenix.abastock.core.data.remote.interceptor

import android.util.Log
import com.softgenix.abastock.core.data.local.SessionEvent
import com.softgenix.abastock.core.data.local.SessionEventBus
import com.softgenix.abastock.core.data.local.TokenManager
import com.softgenix.abastock.core.data.remote.api.RefreshTokenApi
import com.softgenix.abastock.core.data.remote.models.RefreshTokenRequestDto
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val refreshTokenApi: RefreshTokenApi,
    private val sessionEventBus: SessionEventBus
) : Interceptor {

    private val mutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenManager.getAccessToken()
        val request = chain.request().newBuilder()
            .apply {
                if (token != null) {
                    header("Authorization", "Bearer $token")
                    Log.d("AuthInterceptor", "Enviando petición con token: ${token.take(10)}...")
                } else {
                    Log.d("AuthInterceptor", "ℹPetición sin token de acceso")
                }
            }
            .build()

        val response = chain.proceed(request)

        if (response.code == 401) {
            Log.w("AuthInterceptor", "Error 401 detectado en: ${request.url}")
            response.close()
            return tryRefresh(chain)
        }

        return response
    }

    private fun tryRefresh(chain: Interceptor.Chain): Response {
        return runBlocking {
            mutex.withLock {
                val currentToken = tokenManager.getAccessToken()
                val refreshToken = tokenManager.getRefreshToken()

                if (refreshToken == null) {
                    Log.e("AuthInterceptor", "No hay Refresh Token disponible. Forzando Logout.")
                    sessionEventBus.emit(SessionEvent.Logout)
                    return@runBlocking chain.proceed(chain.request())
                }

                Log.d("AuthInterceptor", "Intentando refrescar token...")

                try {
                    val refreshResponse = refreshTokenApi.refresh(RefreshTokenRequestDto(refreshToken))

                    Log.d("AuthInterceptor", "Token refrescado exitosamente")

                    tokenManager.saveTokens(
                        accessToken = refreshResponse.accessToken,
                        refreshToken = refreshResponse.refreshToken
                    )

                    Log.d("AuthInterceptor", "Reintentando petición original con nuevo token")
                    chain.proceed(
                        chain.request().newBuilder()
                            .header("Authorization", "Bearer ${refreshResponse.accessToken}")
                            .build()
                    )
                } catch (e: Exception) {
                    Log.e("AuthInterceptor", "Fallo crítico al refrescar token: ${e.message}")
                    tokenManager.clear()
                    sessionEventBus.emit(SessionEvent.Logout)

                    chain.proceed(chain.request())
                }
            }
        }
    }
}
