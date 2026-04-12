package com.softgenix.abastock.features.inventory.domain.entities

data class InventoryEntitie(
    val inventoryId: String,
    val presentationId: String,
    val productName: String,
    val brandName: String,
    val categoryName: String,
    val currentStock: Double,
    val salePrice: Double,
    val imageUri: String?,
    val barcode: String,
    val unit: String,
    val value: Double
)