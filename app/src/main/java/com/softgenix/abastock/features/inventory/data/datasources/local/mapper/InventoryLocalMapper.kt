package com.softgenix.abastock.features.inventory.data.datasources.local.mapper

import com.softgenix.abastock.core.database.entities.LocalInventoryEntity
import com.softgenix.abastock.features.inventory.domain.entities.InventoryItem
import com.softgenix.abastock.features.inventory.domain.entities.ScannedProduct

fun LocalInventoryEntity.toDomain(): InventoryItem {
    return InventoryItem(
        inventoryId = this.inventoryId,
        presentationId = this.presentationId,
        productName = this.productName,
        brandName = this.brandName,
        categoryName = this.categoryName,
        currentStock = this.currentStock,
        minStockAlert = 0.0,
        salePrice = this.salePrice,
        unit = this.unit,
        value = 0.0,
        barcode = this.barcode,
        imageUri = this.imageUrl
    )
}

fun InventoryItem.toLocalEntity(): LocalInventoryEntity {
    return LocalInventoryEntity(
        inventoryId = this.inventoryId,
        presentationId = this.presentationId,
        productName = this.productName,
        brandName = this.brandName,
        categoryName = this.categoryName,
        currentStock = this.currentStock,
        salePrice = this.salePrice,
        imageUrl = this.imageUri,
        barcode = this.barcode,
        unit = this.unit
    )
}

fun LocalInventoryEntity.toScannedProduct(): ScannedProduct {
    return ScannedProduct(
        id = this.inventoryId,
        presentationId = this.presentationId,
        productName = this.productName,
        brandName = this.brandName,
        categoryName = this.categoryName,
        stock = this.currentStock,
        imageUrl = this.imageUrl,
        barcode = this.barcode,
        unit = this.unit,
        value = 0.0
    )
}