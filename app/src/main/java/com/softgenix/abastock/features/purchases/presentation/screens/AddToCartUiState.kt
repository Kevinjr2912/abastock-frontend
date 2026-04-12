package com.softgenix.abastock.features.purchases.presentation.screens

import com.softgenix.abastock.features.inventory.domain.entities.ScannedProduct

data class AddToCartUiState(
    val product: ScannedProduct? = null,
    val quantity: String = "1",
    val costPrice: String = "",
    val salePrice: String = "",
    val isLoading: Boolean = false,
    val isAdded: Boolean = false,
    val error: String? = null
)