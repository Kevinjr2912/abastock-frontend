package com.softgenix.abastock.features.inventory.domain.entities

data class ScannedProduct(
    val id: String,
    val productName: String,
    val brandName: String,
    val categoryName: String,
    val stock: Double,
    val imageUrl: String?,
    val barcode: String,
    val unit: String,
    val value: Double
)