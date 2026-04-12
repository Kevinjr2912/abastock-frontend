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
import androidx.work.WorkerParameters
import com.softgenix.abastock.core.database.dao.PurchaseDao
import com.softgenix.abastock.core.database.entities.LocalPurchaseEntity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import java.io.File
import java.io.FileOutputStream


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
                return Result.success()
            }

            val file = createPdfReport(purchases)
            showNotification("Reporte de compras Listo", "El PDF se guardó en Descargas")
            Result.success()

        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun createPdfReport(purchases: List<LocalPurchaseEntity>): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(420, 700, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

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

        // encabezado del documento
        canvas.drawText("REPORTE SEMANAL DE COMPRAS", 20f, 40f, titlePaint)
        canvas.drawText("Abastock App - Control de Inventario", 20f, 60f, textPaint)
        canvas.drawLine(20f, 75f, 400f, 75f, linePaint)

        // encabezados de la tabla
        var yPos = 100f
        canvas.drawText("FECHA", 20f, yPos, headerPaint)
        canvas.drawText("PRODUCTOS", 120f, yPos, headerPaint)
        canvas.drawText("TOTAL", 340f, yPos, headerPaint)

        yPos += 15f
        canvas.drawLine(20f, yPos, 400f, yPos, linePaint)
        yPos += 20f

        var grandTotal = 0.0

        // llenado de la tabla
        purchases.forEach { purchase ->
            val cleanDate = purchase.date.substringBefore("T")
            canvas.drawText(cleanDate, 20f, yPos, textPaint)
            canvas.drawText("$${String.format("%.2f", purchase.totalCost)}", 340f, yPos, textPaint)
            val productos = purchase.itemsSummary.split(", ")
            var productY = yPos

            productos.forEach { producto ->
                canvas.drawText("• $producto", 120f, productY, textPaint)
                productY += 15f
            }

            grandTotal += purchase.totalCost

            yPos = productY + 10f
            canvas.drawLine(20f, yPos, 400f, yPos, linePaint) // Raya separadora sutil
            yPos += 20f
        }

        yPos += 10f
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