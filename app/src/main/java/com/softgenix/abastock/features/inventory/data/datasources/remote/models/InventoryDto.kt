package com.softgenix.abastock.features.inventory.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

data class InventoryDto(
    @SerializedName("inventory_id") val inventoryId: String,
    @SerializedName("presentation_id") val presentationId: String,
    @SerializedName("product_name") val productName: String,
    @SerializedName("brand_name") val brandName: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("current_stock") val currentStock: String,
    @SerializedName("sale_price") val salePrice: String,
    @SerializedName("image_uri") val imageUri: String?,
    val unit: String,
    val value: String,
    val barcode: String
)
data class InventoryScanDto(
    val inventory_id: String,
    val current_stock: String,
    val product_name: String,
    val brand_name: String,
    val category_name: String,
    val image_uri: String?,
    val value: String,
    val unit: String,
    val barcode: String
)

data class InventoryResponse(
    val success: Boolean,
    val message: String,
    val data: List<InventoryDto>
)