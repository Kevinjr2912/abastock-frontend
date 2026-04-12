package com.softgenix.abastock.features.purchases.domain.entities

data class SupplyTransactionItem(
    val itemId: String,
    val presentationId: String,
    val inventoryId: String,
    val quantity: Int,
    val costPrice: Double,
    val salePrice: Double,
    val name: String = "",
    val brand: String = "",
    val imageUrl: String? = null
)