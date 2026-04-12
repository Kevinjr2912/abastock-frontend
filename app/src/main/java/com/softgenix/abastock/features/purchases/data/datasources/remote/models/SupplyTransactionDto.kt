package com.softgenix.abastock.features.purchases.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

data class SupplyTransactionRequestDto(
    @SerializedName("purchaseId") val purchaseId: String,
    @SerializedName("storeId") val storeId: String,
    @SerializedName("purchaseDate") val purchaseDate: String,
    @SerializedName("totalCost") val totalCost: Double,
    @SerializedName("purchaseItems") val purchaseItems: List<SupplyTransactionItemDto>
)

data class SupplyTransactionItemDto(
    @SerializedName("purchaseItemId") val purchaseItemId: String,
    @SerializedName("presentationId") val presentationId: String,
    @SerializedName("inventoryId") val inventoryId: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("costPrice") val costPrice: Double,
    @SerializedName("salePrice") val salePrice: Double
)

data class SupplyResponseDto(
    val message: String
)