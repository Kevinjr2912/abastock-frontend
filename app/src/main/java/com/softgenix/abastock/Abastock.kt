package com.softgenix.abastock

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.softgenix.abastock.core.notifications.service.AbastockFirebaseMessagingService
import com.softgenix.abastock.features.reporting.data.workers.PurchaseReportWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class AbastockApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        setupDailyReport()
    }

    private fun createNotificationChannels() {
        val alertsChannel = NotificationChannel(
            AbastockFirebaseMessagingService.CHANNEL_ID,
            "Alertas Abastock",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Resúmenes diarios de ventas e inventario"
            enableVibration(true)
        }

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(alertsChannel)
    }
    private fun setupDailyReport() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()


        val firstRequest = OneTimeWorkRequestBuilder<PurchaseReportWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "PURCHASE_REPORT_RECURSIVE",
            ExistingWorkPolicy.KEEP,
            firstRequest
        )
    }
}