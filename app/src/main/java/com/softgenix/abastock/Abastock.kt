package com.softgenix.abastock

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
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
        setupDailyReport()
    }

    private fun setupDailyReport() {
        //no ejecutar si ya se va a apgar el phone
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        // 2aca se pone cada cuanto
        val dailyRequest = PeriodicWorkRequestBuilder<PurchaseReportWorker>(
            1, TimeUnit.DAYS
        ).setConstraints(constraints).build()


        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "PURCHASE_REPORT_DAILY",
            ExistingPeriodicWorkPolicy.KEEP,
            dailyRequest
        )
    }
}