package com.softgenix.abastock.features.inventory.presentation.screens

import com.softgenix.abastock.features.inventory.domain.entities.InventoryItem

data class InventoryUiState(
    val items: List<InventoryItem> = emptyList(),
    val filteredItems: List<InventoryItem> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: String = "Todos",
    val errorMessage: String? = null
)