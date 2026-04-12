package com.softgenix.abastock.features.inventory.data.datasources.remote.models

data class CreateProductRequestDto(
    val storeId: String,
    val productId: String,
    val name: String,
    val brandId: String,
    val categoryId: String,
    val presentation: PresentationRequestDto
)

data class PresentationRequestDto(
    val presentationId: String,
    val value: Int,
    val unit: String,
    val salePrice: Double,
    val barcode: BarcodeRequestDto
)

data class BarcodeRequestDto(
    val barcodeId: String,
    val code: String,
    val isActive: Boolean
)

// Para los get de marcas y categorías
data class BrandDto(val brandId: String, val name: String)
data class CategoryDto(val categoryId: String, val name: String)