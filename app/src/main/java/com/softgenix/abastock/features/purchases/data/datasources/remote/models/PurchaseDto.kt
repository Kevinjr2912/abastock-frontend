package com.softgenix.abastock.features.purchases.data.datasources.remote.models

data class CreatePurchaseRequestDto(
    val purchaseId: String,
    val storeId: String,
    val purchaseDate: String,
    val totalCost: Double,
    val purchaseItems: List<PurchaseItemDto>
)

data class PurchaseItemDto(
    val purchaseItemId: String,
    val presentationId: String,
    val inventoryId: String,
    val quantity: Int,
    val costPrice: Double,
    val salePrice: Double
)

data class PurchaseResponseDto(
    val message: String
)