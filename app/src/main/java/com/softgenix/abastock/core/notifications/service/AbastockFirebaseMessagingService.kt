package com.softgenix.abastock.core.notifications.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.softgenix.abastock.MainActivity
import com.softgenix.abastock.R
import com.softgenix.abastock.core.data.local.TokenManager
import com.softgenix.abastock.core.notifications.data.FcmTokenManager
import com.softgenix.abastock.features.communications.domain.repositories.CommunicationsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AbastockFirebaseMessagingService : FirebaseMessagingService() {

    @Inject lateinit var fcmTokenManager: FcmTokenManager
    @Inject lateinit var tokenManager: TokenManager
    @Inject lateinit var communicationsRepository: CommunicationsRepository

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    companion object {
        const val CHANNEL_ID = "abastock_alerts"
        private const val NOTIFICATION_ID = 1001
    }

    // Token lifecycle

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        fcmTokenManager.saveToken(token)

        val session = tokenManager.getSession() ?: return
        serviceScope.launch {
            communicationsRepository.registerDeviceToken(session.userId, token)
                .onFailure { android.util.Log.e("FCM", "onNewToken — error al registrar: ${it.message}") }
        }
    }

    // Message received (app en foreground)

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        android.util.Log.d("FCM_DEBUG", "onMessageReceived() llamado!")
        android.util.Log.d("FCM_DEBUG", "notification: title=${message.notification?.title}, body=${message.notification?.body}")
        android.util.Log.d("FCM_DEBUG", "data: ${message.data}")

        val title = message.notification?.title
            ?: message.data["title"]
            ?: "Abastock"
        val body = message.notification?.body
            ?: message.data["body"]
            ?: "Tienes un nuevo resumen"

        showNotification(title, body)
    }

    // Notification builder

    private fun showNotification(title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_inventory)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}