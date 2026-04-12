package com.softgenix.abastock.features.purchases.presentation.screens

import com.softgenix.abastock.features.purchases.domain.entities.SupplyTransactionItem

data class SupplyUiState(
    val cartItems: List<SupplyTransactionItem> = emptyList(),
    val totalInvestment: Double = 0.0,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)