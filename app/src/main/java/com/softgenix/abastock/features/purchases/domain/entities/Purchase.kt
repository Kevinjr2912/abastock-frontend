package com.softgenix.abastock.features.purchases.domain.entities

data class Purchase(
    val id: String,
    val storeId: String,
    val items: List<PurchaseItem>,
    val totalCost: Double,
    val date: String
)