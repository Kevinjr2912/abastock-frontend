package com.softgenix.abastock.features.purchases.presentation.screens

import com.softgenix.abastock.features.purchases.domain.entities.PurchaseItem

data class PurchaseUiState(
    val cartItems: List<PurchaseItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
)