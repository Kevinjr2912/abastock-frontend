package com.softgenix.abastock.features.sales.data.datasources.remote.dto

import com.google.gson.annotations.SerializedName

//del post
data class SaleRequestDto(
    @SerializedName("saleId") val saleId: String,
    @SerializedName("storeId") val storeId: String,
    @SerializedName("saleDate") val saleDate: String,
    @SerializedName("totalAmount") val totalAmount: Double,
    @SerializedName("saleItems") val saleItems: List<SaleItemRequestDto>
)

data class SaleItemRequestDto(
    @SerializedName("saleItemId") val saleItemId: String,
    @SerializedName("presentationId") val presentationId: String,
    @SerializedName("inventoryId") val inventoryId: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("salePrice") val salePrice: Double,
    @SerializedName("subtotal") val subtotal: Double
)

//del get
data class SalesHistoryResponseDto(
    @SerializedName("summary") val summary: SalesSummaryDto,
    @SerializedName("sales") val sales: List<SaleRecordDto>
)

data class SalesSummaryDto(
    @SerializedName("totalRevenue") val totalRevenue: Double,
    @SerializedName("transactionCount") val transactionCount: Int
)

data class SaleRecordDto(
    @SerializedName("saleId") val saleId: String,
    @SerializedName("time") val time: String,
    @SerializedName("totalAmount") val totalAmount: Double,
    @SerializedName("totalItems") val totalItems: Int,
    @SerializedName("items") val items: List<SaleItemDto>
)

data class SaleItemDto(
    @SerializedName("productName") val productName: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("subtotal") val subtotal: Double
)