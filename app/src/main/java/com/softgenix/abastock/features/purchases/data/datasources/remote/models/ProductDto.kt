package com.softgenix.abastock.features.purchases.data.datasources.remote.models

data class ProductDto(
    val productId: String,
    val name: String,
    val brandName: String,
    val categoryName: String,
    val currentStock: Double,
    val salePrice: Double,
    val presentationId: String,
    val inventoryId: String,
)

data class PurchaseRequestDto(
    val storeId: String,
    val totalCost: Double,
    val items: List<PurchaseItemRequestDto>
)

data class PurchaseItemRequestDto(
    val presentationId: String,
    val quantity: Double,
    val costPrice: Double
)