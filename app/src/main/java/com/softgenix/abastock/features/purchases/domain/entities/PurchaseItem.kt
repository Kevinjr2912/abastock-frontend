package com.softgenix.abastock.features.purchases.domain.entities

data class PurchaseItem(
    val presentationId: String,
    val inventoryId: String,
    val name: String,
    val quantity: Double,
    val costPrice: Double,
    val salePrice: Double,
    val barcode: String
)
