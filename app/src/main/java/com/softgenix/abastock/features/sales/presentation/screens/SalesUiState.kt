package com.softgenix.abastock.features.sales.presentation.screens

data class SalesUiState(
    val cartItems: List<SaleCartItem> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val lastSaleTotal: Double = 0.0
) {
    val totalItems: Int get() = cartItems.sumOf { it.quantity }
    val totalAmount: Double get() = cartItems.sumOf { it.salePrice * it.quantity }
}

data class SaleCartItem(
    val cartItemId: String = java.util.UUID.randomUUID().toString(),
    val presentationId: String,
    val inventoryId: String,
    val name: String,
    val salePrice: Double,
    val quantity: Int,
    val imageUrl: String? = null
)