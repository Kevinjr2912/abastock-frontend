package com.softgenix.abastock.features.reporting.data.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.softgenix.abastock.core.database.dao.PurchaseDao
import com.softgenix.abastock.core.database.entities.LocalPurchaseEntity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class PurchaseReportWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ReportWorkerEntryPoint {
        fun getPurchaseDao(): PurchaseDao
    }

    override suspend fun doWork(): Result {
        return try {
            val entryPoint = EntryPointAccessors.fromApplication(
                applicationContext,
                ReportWorkerEntryPoint::class.java
            )
            val purchaseDao = entryPoint.getPurchaseDao()
            val purchases = purchaseDao.getAllPurchases()

            if (purchases.isEmpty()) {
                showNotification("Reporte Omitido", "No hay compras registradas esta semana.")
                reScheduleSelf()
                return Result.success()
            }

            val file = createPdfReport(purchases)
            showNotification("Reporte de compras Listo", "El PDF se guardó en Descargas")

            reScheduleSelf()

            Result.success()

        } catch (e: Exception) {
            reScheduleSelf()
            Result.failure()
        }
    }

    private fun reScheduleSelf() {
        val nextRequest = OneTimeWorkRequestBuilder<PurchaseReportWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            "PURCHASE_REPORT_RECURSIVE",
            ExistingWorkPolicy.REPLACE,
            nextRequest
        )
    }

    private fun createPdfReport(purchases: List<LocalPurchaseEntity>): File {
        val pdfDocument = PdfDocument()
        val pageHeight = 700
        val pageWidth = 420
        var pageNumber = 1

        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas: Canvas = page.canvas

        val titlePaint = Paint().apply {
            textSize = 16f
            isFakeBoldText = true
            color = android.graphics.Color.rgb(33, 33, 33)
        }
        val headerPaint = Paint().apply {
            textSize = 12f
            isFakeBoldText = true
            color = android.graphics.Color.GRAY
        }
        val textPaint = Paint().apply {
            textSize = 11f
            color = android.graphics.Color.BLACK
        }
        val linePaint = Paint().apply {
            color = android.graphics.Color.LTGRAY
            strokeWidth = 1f
        }

        fun drawHeaders() {
            canvas.drawText(if (pageNumber == 1) "REPORTE SEMANAL DE COMPRAS" else "REPORTE DE COMPRAS (Cont.)", 20f, 40f, titlePaint)
            if (pageNumber == 1) canvas.drawText("Abastock App - Control de Inventario", 20f, 60f, textPaint)
            canvas.drawLine(20f, 75f, 400f, 75f, linePaint)

            canvas.drawText("FECHA", 20f, 100f, headerPaint)
            canvas.drawText("PRODUCTOS", 120f, 100f, headerPaint)
            canvas.drawText("TOTAL", 340f, 100f, headerPaint)
            canvas.drawLine(20f, 115f, 400f, 115f, linePaint)
        }

        drawHeaders()
        var yPos = 135f
        var grandTotal = 0.0
        val limitY = 620f

        purchases.forEach { purchase ->
            val productos = purchase.itemsSummary.split(", ")

            val estimatedHeight = 20f + (productos.size * 15f) + 10f

            if (yPos + estimatedHeight > limitY) {

                pdfDocument.finishPage(page)

                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas

                drawHeaders()
                yPos = 135f
            }

            val cleanDate = purchase.date.substringBefore("T")
            canvas.drawText(cleanDate, 20f, yPos, textPaint)
            canvas.drawText("$${String.format("%.2f", purchase.totalCost)}", 340f, yPos, textPaint)

            var productY = yPos
            productos.forEach { producto ->
                canvas.drawText("• $producto", 120f, productY, textPaint)
                productY += 15f
            }

            grandTotal += purchase.totalCost

            yPos = productY + 10f
            canvas.drawLine(20f, yPos, 400f, yPos, linePaint)
            yPos += 20f
        }

        if (yPos + 30f > limitY) {
            pdfDocument.finishPage(page)
            pageNumber++
            pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            page = pdfDocument.startPage(pageInfo)
            canvas = page.canvas
            yPos = 60f
        } else {
            yPos += 10f
        }

        titlePaint.textSize = 14f
        canvas.drawText("TOTAL INVERTIDO: $${String.format("%.2f", grandTotal)}", 190f, yPos, titlePaint)

        pdfDocument.finishPage(page)

        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, "Reporte_Compras_${System.currentTimeMillis()}.pdf")

        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        return file
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "report_channel"

        val channel = NotificationChannel(channelId, "Reportes", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_menu_save)
            .build()

        notificationManager.notify(1, notification)
    }
}