package com.softgenix.abastock.features.inventory.domain.entities

data class InventoryItem(
    val inventoryId: String,
    val presentationId: String,
    val productName: String,
    val brandName: String,
    val categoryName: String,
    val currentStock: Double,
    val minStockAlert: Double,
    val salePrice: Double,
    val unit: String,
    val value: Double,
    val barcode: String,
    val imageUri: String?
)


