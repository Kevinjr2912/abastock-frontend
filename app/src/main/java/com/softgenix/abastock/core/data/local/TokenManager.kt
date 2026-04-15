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
    private val TEMPORARY_STORE_ID = "6b5c757d-68b5-4ec3-b47a-8e1cb8bd9ae8"

    fun saveTokens(accessToken: String, refreshToken: String) {
        prefs.edit()
            .putString("access_token", accessToken)
            .putString("refresh_token", refreshToken)
            .apply()
        decodeAndSaveSession(accessToken)
    }

    fun getAccessToken(): String? = prefs.getString("access_token", null)
    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)
    fun getStoreId(): String {
        return prefs.getString("GLOBAL_STORE_ID", TEMPORARY_STORE_ID) ?: TEMPORARY_STORE_ID
    }
    fun saveStoreId(storeId: String) {
        prefs.edit().putString("GLOBAL_STORE_ID", storeId).apply()
    }

    fun getSession(): UserSession? {
        val userId = prefs.getString("user_id", null) ?: return null
        return UserSession(
            userId = userId,
            email = prefs.getString("email", "") ?: "",
            name = prefs.getString("name", "") ?: "",
            storeId = getStoreId(),
            storeName = prefs.getString("store_name", null)
        )
    }

    fun clear() = prefs.edit().clear().apply()

    private fun decodeAndSaveSession(accessToken: String) {
        try {
            val payload = decodeJwtPayload(accessToken)
            val backendStoreId = payload.optString("storeId", "")
            if (backendStoreId.isNotEmpty()) {
                saveStoreId(backendStoreId)
            }
            prefs.edit()
                .putString("user_id", payload.getString("userId"))
                .putString("email", payload.getString("email"))
                .putString("name", payload.getString("name"))
                .putString("store_name", payload.optString("storeName", null))
                .apply()
        } catch (e: Exception) {
            e.printStackTrace()
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