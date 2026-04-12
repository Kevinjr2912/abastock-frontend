package com.softgenix.abastock.features.sales.domain.entities

data class SaleTransaction(
    val saleId: String,
    val storeId: String,
    val saleDate: String,
    val totalAmount: Double,
    val items: List<SaleTransactionItem>
)

data class SaleTransactionItem(
    val saleItemId: String,
    val presentationId: String,
    val inventoryId: String,
    val quantity: Int,
    val salePrice: Double,
    val subtotal: Double
)

// --- PARA RECIBIR EL HISTORIAL (GET) ---
data class DailySalesData(
    val totalRevenue: Double,
    val transactionCount: Int,
    val salesHistory: List<SaleHistoryRecord>
)

data class SaleHistoryRecord(
    val saleId: String,
    val time: String,
    val totalAmount: Double,
    val totalItems: Int,
    val items: List<SaleHistoryItem>
)

data class SaleHistoryItem(
    val productName: String,
    val quantity: Int,
    val subtotal: Double
)