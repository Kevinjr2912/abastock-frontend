package com.softgenix.abastock.core.notifications.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmTokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("fcm_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("fcm_token", token).apply()
    }

    fun getToken(): String? = prefs.getString("fcm_token", null)

    fun clearToken() = prefs.edit().remove("fcm_token").apply()
}