package com.softgenix.abastock.features.inventory.data.datasources.remote.models

data class InventoryScanResponse(
    val inventory_id: String,
    val presentation_id: String,
    val current_stock: String,
    val product_name: String,
    val brand_name: String,
    val category_name: String,
    val image_uri: String?,
    val value: String,
    val unit: String,
    val barcode: String
)