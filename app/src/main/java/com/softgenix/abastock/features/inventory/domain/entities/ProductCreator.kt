package com.softgenix.abastock.features.inventory.domain.entities

data class NewProduct(
    val productId: String,
    val name: String,
    val brandId: String,
    val categoryId: String,
    val presentation: NewPresentation,
    val imageUri: String? = null
)

data class NewPresentation(
    val presentationId: String,
    val value: Int,
    val unit: String,
    val salePrice: Double,
    val barcode: NewBarcode
)

data class NewBarcode(
    val barcodeId: String,
    val code: String,
    val isActive: Boolean = true
)

data class Brand(
    val id: String,
    val name: String
)

data class Category(
    val id: String,
    val name: String
)