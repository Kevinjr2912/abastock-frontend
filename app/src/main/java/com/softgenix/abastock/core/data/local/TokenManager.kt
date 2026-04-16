package com.softgenix.abastock.core.data.local

import android.content.Context
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun saveTokens(accessToken: String, refreshToken: String) {
        prefs.edit()
            .putString("access_token", accessToken)
            .putString("refresh_token", refreshToken)
            .apply()
        decodeAndSaveSession(accessToken)
    }

    fun getAccessToken(): String? = prefs.getString("access_token", null)
    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)

    fun getSession(): UserSession? {
        val userId = prefs.getString("user_id", null) ?: return null
        return UserSession(
            userId = userId,
            email = prefs.getString("email", "") ?: "",
            name = prefs.getString("name", "") ?: "",
            storeId = prefs.getString("store_id", null),
            storeName = prefs.getString("store_name", null)
        )
    }

    fun clear() = prefs.edit().clear().apply()

    private fun decodeAndSaveSession(accessToken: String) {
        try {
            val payload = decodeJwtPayload(accessToken)
            prefs.edit()
                .putString("user_id", payload.getString("sub"))
                .putString("email", payload.optString("email", ""))
                .putString("name", payload.optString("name", ""))
                .putString("store_id", payload.optString("storeId", null))
                .putString("store_name", payload.optString("storeName", null))
                .apply()
        } catch (e: Exception) {
            android.util.Log.e("FCM_DEBUG", "Error decodificando JWT: ${e.message}", e)  // 👈 visible en logcat
        }
    }

    private fun decodeJwtPayload(token: String): JSONObject {
        val payload = token.split(".")[1]
        val decoded = Base64.decode(
            payload,
            Base64.URL_SAFE or Base64.NO_PADDING
        )
        return JSONObject(String(decoded))
    }
}