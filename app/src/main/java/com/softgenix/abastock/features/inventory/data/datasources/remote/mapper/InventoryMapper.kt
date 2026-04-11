package com.softgenix.abastock.features.inventory.data.datasources.remote.mapper

import com.softgenix.abastock.features.inventory.data.datasources.remote.models.BarcodeRequestDto
import com.softgenix.abastock.features.inventory.data.datasources.remote.models.BrandDto
import com.softgenix.abastock.features.inventory.data.datasources.remote.models.CategoryDto
import com.softgenix.abastock.features.inventory.data.datasources.remote.models.CreateProductRequestDto
import com.softgenix.abastock.features.inventory.data.datasources.remote.models.InventoryDto
import com.softgenix.abastock.features.inventory.data.datasources.remote.models.InventoryScanResponse
import com.softgenix.abastock.features.inventory.data.datasources.remote.models.PresentationRequestDto
import com.softgenix.abastock.features.inventory.domain.entities.Brand
import com.softgenix.abastock.features.inventory.domain.entities.Category
import com.softgenix.abastock.features.inventory.domain.entities.InventoryItem
import com.softgenix.abastock.features.inventory.domain.entities.NewProduct
import com.softgenix.abastock.features.inventory.domain.entities.ScannedProduct

fun InventoryDto.toDomain(): InventoryItem {
    return InventoryItem(
        inventoryId = this.inventoryId,
        presentationId = this.presentationId,
        productName = this.productName,
        brandName = this.brandName,
        categoryName = this.categoryName,
        currentStock = this.currentStock.toDoubleOrNull() ?: 0.0,
        minStockAlert = 0.0,
        salePrice = this.salePrice.toDoubleOrNull() ?: 0.0,
        unit = this.unit,
        value = this.value.toDoubleOrNull() ?: 0.0,
        barcode = this.barcode,
        imageUri = this.imageUri
    )
}
fun InventoryScanResponse.toDomain(): ScannedProduct {
    return ScannedProduct(
        id = this.inventory_id,
        presentationId = this.presentation_id ?: "",
        productName = this.product_name,
        brandName = this.brand_name,
        categoryName = this.category_name,
        stock = this.current_stock.toDoubleOrNull() ?: 0.0,
        imageUrl = this.image_uri,
        barcode = this.barcode,
        unit = this.unit,
        value = this.value.toDoubleOrNull() ?: 0.0
    )
}


fun BrandDto.toDomain() = Brand(id = this.brandId, name = this.name)
fun CategoryDto.toDomain() = Category(id = this.categoryId, name = this.name)

fun NewProduct.toDto(storeId: String) = CreateProductRequestDto(
    storeId = storeId,
    productId = this.productId,
    name = this.name,
    brandId = this.brandId,
    categoryId = this.categoryId,
    presentation = PresentationRequestDto(
        presentationId = this.presentation.presentationId,
        value = this.presentation.value,
        unit = this.presentation.unit,
        salePrice = this.presentation.salePrice,
        barcode = BarcodeRequestDto(
            barcodeId = this.presentation.barcode.barcodeId,
            code = this.presentation.barcode.code,
            isActive = this.presentation.barcode.isActive
        )
    )
)