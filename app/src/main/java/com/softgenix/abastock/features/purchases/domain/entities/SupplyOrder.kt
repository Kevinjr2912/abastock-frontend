package com.softgenix.abastock.features.purchases.domain.entities

data class SupplyTransaction(
    val transactionId: String,
    val storeId: String,
    val date: String,
    val totalCost: Double,
    val items: List<SupplyTransactionItem>
)