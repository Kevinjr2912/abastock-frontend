package com.softgenix.abastock.features.inventory.presentation.screens

import com.softgenix.abastock.features.inventory.domain.entities.Brand
import com.softgenix.abastock.features.inventory.domain.entities.Category

data class CreateProductUiState(
    val isLoading: Boolean = false,
    val selectedImageUri: android.net.Uri? = null,
    val name: String = "",
    val brands: List<Brand> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedBrand: Brand? = null,
    val selectedCategory: Category? = null,
    val value: String = "1",
    val unit: String = "L",
    val salePrice: String = "",
    val error: String? = null,
    val isSuccess: Boolean = false
)